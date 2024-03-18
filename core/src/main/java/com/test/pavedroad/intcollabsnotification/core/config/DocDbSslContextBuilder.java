package com.test.pavedroad.intcollabsnotification.core.config;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Iterator;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import com.mongodb.MongoClientException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DocDbSslContextBuilder {
	public SSLContext buildSSLContext() {
		try (InputStream is = getClass().getResourceAsStream("/rds-combined-ca-bundle.pem")) {
			TrustManagerFactory trustManagerFactory = TrustManagerFactory
					.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			{
				KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
				ks.load(null);
				Iterator<X509Certificate> certIterator = (Iterator<X509Certificate>)
						CertificateFactory.getInstance("X.509").generateCertificates(is).iterator();
				int i = 0;
				while (certIterator.hasNext()) {
					ks.setCertificateEntry("RDSCaCert" +i, certIterator.next());
					i += 1;
				}
				trustManagerFactory.init(ks);
			}
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
			log.info("SslContext successfully created : " + sslContext.getDefaultSSLParameters());
			return sslContext;
		} catch (Exception e) {
			throw new MongoClientException("Failure to construct SSL Context for MongoDB", e);
		}
	}
}
