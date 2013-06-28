class ReturnInnerClass {

  public OAuth2Operations getOAuthOperations() {
    return new OAuth2Operations() {
      public String buildAuthorizeUrl(GrantType grantType, OAuth2Parameters params) {
        return null;
      }
      public String buildAuthenticateUrl(GrantType grantType, OAuth2Parameters params) {
        return null;
      }
      public AccessGrant exchangeForAccess(String authorizationGrant, String redirectUri, MultiValueMap<String, String> additionalParameters) {
        return null;
      }       
      public AccessGrant exchangeCredentialsForAccess(String username, String password, MultiValueMap<String, String> additionalParameters) {
        return null;
      }
      public AccessGrant refreshAccess(String refreshToken, MultiValueMap<String, String> additionalParameters) {
        return new AccessGrant("765432109", "read", "654321098", 3600);
      }
      public AccessGrant refreshAccess(String refreshToken, String scope, MultiValueMap<String, String> additionalParameters) {
        return new AccessGrant("765432109", "read", "654321098", 3600);
      }
      public AccessGrant authenticateClient() {
        return null;
      }
      public AccessGrant authenticateClient(String scope) {
        return null;
      }
    };
  }
}