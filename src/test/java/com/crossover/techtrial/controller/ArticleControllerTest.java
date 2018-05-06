package com.crossover.techtrial.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.crossover.techtrial.model.Article;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ArticleControllerTest {

  @Autowired
  private TestRestTemplate template;

  @Before
  public void setup() throws Exception {

  }

  @Test
  public void testArticleShouldBeCreated() throws Exception {
    HttpEntity<Object> article = getHttpEntity(
        "{\"email\": \"user1@gmail.com\", \"title\": \"hello\" }");
    ResponseEntity<Article> resultAsset = template.postForEntity("/articles", article,
        Article.class);
    Assert.assertNotNull(resultAsset.getBody().getId());
    testArticleShouldBeThere(resultAsset.getBody().getId());
    testArticleShouldBeUpdated(resultAsset.getBody().getId());
    testArticleShouldBeDeleted(resultAsset.getBody().getId());
  }
  
  private HttpEntity<Object> getHttpEntity(Object body) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return new HttpEntity<Object>(body, headers);
  }
  
  private void testArticleShouldBeThere(Long id){
	    ResponseEntity<Article> resultAsset = template.getForEntity("/articles/"+id.toString(),
	        Article.class);
	    Assert.assertNotNull(resultAsset.getBody().getEmail());
  }

  private void testArticleShouldBeUpdated(Long id){
	  ResponseEntity<Article> resultAssetData = template.getForEntity("/articles/"+id.toString(),
		        Article.class);
	  String email = "ramamoorthypandiyaraja@gmail.com";
	  resultAssetData.getBody().setEmail(email);
	  template.put("/articles/"+id.toString(), resultAssetData.getBody(),
	        Article.class);
	  ResponseEntity<Article> resultAsset = template.getForEntity("/articles/"+id.toString(),
		        Article.class);
	    Assert.assertEquals(resultAsset.getBody().getEmail(), email);
  }
  
  private void testArticleShouldBeDeleted(Long id) {
	  template.delete("/articles/"+id.toString());
	  ResponseEntity<Article> resultAsset = template.getForEntity("/articles/"+id.toString(),
		        Article.class);
	  Assert.assertNotEquals(resultAsset.getStatusCode(), HttpStatus.OK);
  }
}
