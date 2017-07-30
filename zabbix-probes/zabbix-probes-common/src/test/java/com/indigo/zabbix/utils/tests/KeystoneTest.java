package com.indigo.zabbix.utils.tests;

import com.indigo.zabbix.utils.KeystoneClient;
import com.indigo.zabbix.utils.KeystoneTokenProvider;
import com.indigo.zabbix.utils.beans.KeystoneScopedTokenRequest;
import com.indigo.zabbix.utils.beans.OpenstackProjectsInfo;

import feign.Response;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by jose on 5/04/17.
 */
public class KeystoneTest {

  public static final String ACCESS_TOKEN = "access";
  public static final String UNSCOPED_TOKEN = "unscoped";
  public static final String SCOPED_TOKEN = "scoped";

  private KeystoneTokenProvider provider = Mockito.mock(KeystoneTokenProvider.class);

  private List<String> projects = new ArrayList<>();

  private Response responseToken(String token) {

    Map<String, Collection<String>> headers = new HashMap<>();

    if (token != null) {
      Collection<String> tokens = new ArrayList<>();
      tokens.add(token);
      headers.put(KeystoneClient.TOKEN_RESULT_HEADER, tokens);
    }

    return Response.create(200, null, headers, (Response.Body) null);
  }



  @Before
  public void prepare() {

    projects.add("project1");
    projects.add("project2");

    Mockito.when(provider.getKeystoneToken(Matchers.anyString(),
        Matchers.anyString(), Matchers.anyString()))
        .thenAnswer(new Answer<Response>() {
          @Override
          public Response answer(InvocationOnMock invocationOnMock) throws Throwable {
            String token = invocationOnMock.getArgumentAt(0, String.class);
            if (ACCESS_TOKEN.equals(token)) {
              return responseToken(UNSCOPED_TOKEN);
            } else {
              return responseToken(null);
            }
          }
        });

    Mockito.when(provider.getScopedToken(Matchers.any(KeystoneScopedTokenRequest.class)))
        .thenAnswer(new Answer<Response>() {
          @Override
          public Response answer(InvocationOnMock invocationOnMock) throws Throwable {
            KeystoneScopedTokenRequest request = invocationOnMock
                .getArgumentAt(0, KeystoneScopedTokenRequest.class);

            if (UNSCOPED_TOKEN.equals(request.getTokenId())) {
              return responseToken(SCOPED_TOKEN);
            } else {
              return responseToken(null);
            }
          }
        });

    Mockito.when(provider.getProjects(Matchers.anyString()))
        .thenAnswer(new Answer<OpenstackProjectsInfo>() {

          @Override
          public OpenstackProjectsInfo answer(InvocationOnMock invocationOnMock) throws Throwable {
            String token = invocationOnMock.getArgumentAt(0, String.class);
              OpenstackProjectsInfo result = new OpenstackProjectsInfo();
              if (UNSCOPED_TOKEN.equals(token)) {
                final List<OpenstackProjectsInfo.Project> projectList = new ArrayList<>();
                projects.forEach(projectName -> {
                  OpenstackProjectsInfo.Project project = new OpenstackProjectsInfo.Project();
                  project.setName(projectName);
                  project.setId(projectName);
                  projectList.add(project);
                });
                result.setProjects(projectList);
              }
              return result;
            }
        });
  }

  @Test
  public void testUnscoped() {
    KeystoneClient client = new KeystoneClient(provider);

    String unscopedToken = client.getUnscopedToken(ACCESS_TOKEN, "indigo-dc", "oidc");

    assert unscopedToken != null;
    assert UNSCOPED_TOKEN.equals(unscopedToken);

    unscopedToken = client.getUnscopedToken("invalid", "indigo-dc", "oidc");
    assert unscopedToken == null;

  }

  @Test
  public void testScoped() {
    KeystoneClient client = new KeystoneClient(provider);

    String scoped = client.getScopedToken(ACCESS_TOKEN, "indigo-dc", "oidc", "project1");

    assert scoped != null;
    assert SCOPED_TOKEN.equals(scoped);

    scoped = client.getScopedToken("invalid", "indigo-dc", "oidc", "project1");

    assert scoped == null;

    scoped = client.getScopedToken(ACCESS_TOKEN, "nonexistent", "indigo-dc", "oidc");

    assert scoped == null;
  }

}
