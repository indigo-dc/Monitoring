package com.indigo.zabbix.utils.beans;

/**
 * Created by jose on 4/04/17.
 */
public class KeystoneScopedTokenRequest {

  private class Authorization {

    private class Identity {

      private class Token {

        private String id;

        public Token(String id) {
          this.id = id;
        }

        public String getId() {
          return id;
        }

        public void setId(String id) {
          this.id = id;
        }
      }

      private String[] methods = {"token"};
      private Token token;


      public Identity(String tokenId) {
        this.token = new Token(tokenId);
      }

      public String[] getMethods() {
        return methods;
      }

      public void setMethods(String[] methods) {
        this.methods = methods;
      }

      public Token getToken() {
        return token;
      }

      public void setToken(Token token) {
        this.token = token;
      }

    }

    private class Scope {

      private class Project {

        private String id;


        public Project(String id) {
          this.id = id;
        }

        public String getId() {
          return id;
        }

        public void setId(String id) {
          this.id = id;
        }
      }

      private Project project;

      public Scope(String projectId) {
        this.project = new Project(projectId);
      }

      public Project getProject() {
        return project;
      }

      public void setProject(Project project) {
        this.project = project;
      }
    }

    private Identity identity;
    private Scope scope;

    public Authorization(String tokenId, String projectId) {
      this.identity = new Identity(tokenId);
      this.scope = new Scope(projectId);
    }

    public Identity getIdentity() {
      return identity;
    }

    public Scope getScope() {
      return scope;
    }
  }

  private Authorization auth;


  public KeystoneScopedTokenRequest(String tokenId, String projectId) {
    this.auth = new Authorization(tokenId, projectId);
  }

  public String getTokenId() {
    return auth.getIdentity().getToken().getId();
  }

  public String getProjectId(Authorization auth) {
    return this.auth.getScope().getProject().getId();
  }

}
