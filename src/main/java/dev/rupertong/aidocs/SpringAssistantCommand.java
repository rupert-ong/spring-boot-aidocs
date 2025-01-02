package dev.rupertong.aidocs;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.core.io.Resource;
import org.springframework.shell.command.annotation.Command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Command
public class SpringAssistantCommand {

  private final ChatClient chatClient;
  private final VectorStore vectorStore;

  @Value("classpath:/prompts/spring-boot-reference.st")
  private Resource sbPromptTemplate;

  public SpringAssistantCommand(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
    this.chatClient = chatClientBuilder.build();
    this.vectorStore = vectorStore;
  }

  // example: q "What is WebClient in Spring"
  @Command(command = "q")
  public String question(@DefaultValue(value = "What is Spring boot") String message) {
    PromptTemplate promptTemplate = new PromptTemplate(sbPromptTemplate);
    Map<String, Object> promptParameters = new HashMap<>();
    promptParameters.put("input", message);
    promptParameters.put("documents", String.join("\n", findSimilarDocuments(message)));

    return chatClient.prompt(promptTemplate.create(promptParameters)).call().content();
  }

  // Only get relevant information from the vector store to send back as response (not entire document)
  private List<String> findSimilarDocuments(String message) {
    List<Document> similarDocuments = vectorStore.similaritySearch(SearchRequest.query(message).withTopK(3));
    return similarDocuments.stream().map(Document::getContent).toList();
  }
}
