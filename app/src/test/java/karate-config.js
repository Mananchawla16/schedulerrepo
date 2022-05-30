function fn() {
    karate.configure('connectTimeout', 5000);
    karate.configure('readTimeout', 500000);
    var port = karate.properties['karate.server.port'];
    if (!port) port = 8443;
    var env = karate.env; // get system property 'karate.env'
    karate.log('karate.env system property was:', env);
    if (!env) {
        env = 'default';
    }
    var config = {
        env: env,
        appId: 'Intuit.platform.servicestesting.testapp2014',
        appSecret: 'preprdTvsqjb6LZl5cvidoS7mnZQ4UXob4cU7455',
        mfaUrlBase: 'https://access-e2e.platform.intuit.com',
        oiiUrlBase: 'https://accounts-e2e.platform.intuit.com',
        downstreamApiUrlBase: 'https://your.downstream.api.url.com'
    };
    if(env !== 'mock') {
        var msaas = karate.read('file:../msaas-config.yaml')
        if( msaas["environments"][env] != null)
            config.baseUrl = msaas["environments"][env]["ingress_endpoint"]
    }
    //Add your environment specific configurations.
    if (env === 'default' || env === 'local') {
        //Add your environment specific configurations.
        config.baseUrl = 'https://localhost:' + port;
        karate.configure('ssl', { trustAll: true })
    }
    else if (env === 'mock') {
        var mockPort = karate.properties['karate.mock.port'];

        if (!mockPort) mockPort = 9000;

        config.baseUrl = 'https://localhost:' + port;
        karate.configure('ssl', { trustAll: true })
        // Set your downstream url to the mock karate server
        config.downstreamApiUrlBase = 'http://localhost:' + mockPort;
        config.mfaUrlBase = 'http://localhost:' + mockPort;
        config.oiiUrlBase = 'http://localhost:' + mockPort;

    }
        else if (env === 'qal-usw2-eks') {
        //Update your configurations for environments
    }
    else if (env === 'e2e-usw2-eks') {
        //Update your configurations for environments
    }

    return config;
}
