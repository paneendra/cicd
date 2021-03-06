import groovy.json.JsonSlurper

//Method for http callouts
static def doRESTHTTPCall(urlString, method, payload, headers)
{
    //log(DEBUG,  "START doRESTHTTPCall")
    //log(INFO, "requestURl is " + urlString)
    def url = new URL(urlString)
    def connection = url.openConnection()
    headers.keySet().each {
        //log(INFO, it + "->" + headers.get(it))
        connection.setRequestProperty(it, headers.get(it))
    }
    connection.doOutput = true
    if (method == "POST")
    {
        connection.setRequestMethod("POST")
        def writer = new OutputStreamWriter(connection.outputStream)
        writer.write(payload)
        writer.flush()
        writer.close()
    }
    else if (method == "GET")
    {
        connection.setRequestMethod("GET")
    }
    connection.connect()
    //log(DEBUG,  "END doRESTHTTPCall")
    return connection
}

//Gets the anypoint platform token
def getAnypointToken()
{
    //log(DEBUG,  "START getAnypointToken")
    def username="SupportNonProd"
    def password="gGUF92NG1\$ww"
    //log(TRACE, "username=" + username)
    // log(TRACE, "password=" + password)
    def urlString = "https://anypoint.mulesoft.com/accounts/login"
    def message = 'username='+username+'&password='+password
    def headers=["Content-Type":"application/x-www-form-urlencoded", "Accept": "application/json"]
    def connection = doRESTHTTPCall(urlString, "POST", message, headers)
    if ( connection.responseCode =~ '2..')
    {
    }else
    {
        throw new Exception("Failed to get the login token!")
    }
    def response = connection.content

    def token = new JsonSlurper().parse(response).access_token
    //log(INFO, "Bearer Token: ${token}")
    //log(DEBUG,  "END getAnypointToken")
    return token
}

return this
//String token = getAnypointToken(props)