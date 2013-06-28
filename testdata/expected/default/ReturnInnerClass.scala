class ReturnInnerClass {

  def getOAuthOperations() : OAuth2Operations =
  {
    return new OAuth2Operations() {
      def
  buildAuthorizeUrl
  (
    grantType : GrantType,
    params : OAuth2Parameters
  ) : String = 
  null
      def
  buildAuthenticateUrl
  (
    grantType : GrantType,
    params : OAuth2Parameters
  ) : String = 
  null
      def
  exchangeForAccess
  (
    authorizationGrant : String,
    redirectUri : String,
    additionalParameters : MultiValueMap[String, String]
  ) : AccessGrant = 
  null       
      def
  exchangeCredentialsForAccess
  (
    username : String,
    password : String,
    additionalParameters : MultiValueMap[String, String]
  ) : AccessGrant = 
  null
      def
  refreshAccess
  (
    refreshToken : String,
    additionalParameters : MultiValueMap[String, String]
  ) : AccessGrant = 
  new AccessGrant("765432109", "read", "654321098", 3600)
      def
  refreshAccess
  (
    refreshToken : String,
    scope : String,
    additionalParameters : MultiValueMap[String, String]
  ) : AccessGrant = 
  new AccessGrant("765432109", "read", "654321098", 3600)
      def authenticateClient() : AccessGrant = null
      def
  authenticateClient
  (
    scope : String
  ) : AccessGrant = 
  null
    }
  }
}