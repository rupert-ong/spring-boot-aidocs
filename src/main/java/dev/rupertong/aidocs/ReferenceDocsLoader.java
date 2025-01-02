package dev.rupertong.aidocs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class ReferenceDocsLoader {
  private static final Logger log = LoggerFactory.getLogger(ReferenceDocsLoader.class);
  private final JdbcClient jdbcClient;
  private final VectorStore vectorStore;

  @Value("classpath:/docs/spring-boot-reference.pdf")
  private Resource pdfResource;

  public ReferenceDocsLoader(JdbcClient jdbcClient, VectorStore vectorStore) {
    this.jdbcClient = jdbcClient;
    this.vectorStore = vectorStore;
  }

  // Populate vector store on post construct since this is a shell application
  // Only do this if the DB does not exist since this is an expensive process
  @PostConstruct
  public void init() {
    Integer count = jdbcClient.sql("select count(*) from vector_store")
        .query(Integer.class)
        .single();

    log.info("Current count of Vector store: {}", count);

    if (count == 0) {
      log.info("Loading Spring Boot Reference PDF into Vector store since it does not exist");

      PdfDocumentReaderConfig config = PdfDocumentReaderConfig.builder()
          .withPageTopMargin(0)
          .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
              .withNumberOfTopTextLinesToDelete(0)
              .build())
          .withPagesPerDocument(1)
          .build();

      PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(pdfResource, config);
      TokenTextSplitter textSplitter = new TokenTextSplitter();
      vectorStore.accept(textSplitter.apply(pdfReader.get()));

      log.info("Application is ready, and vector store loaded with data");
    }
  }
}
