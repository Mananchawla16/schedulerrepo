package com.test.pavedroad.intcollabsnotification.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Iterator;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import com.mongodb.MongoClientException;

import com.test.pavedroad.intcollabsnotification.core.constants.ApplicationConstants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DocDbSslContextBuilder {

	public static final String DEFAULT_CERT_FORMAT = "X.509";
	public static final String DEFAULT_CERT_ALIAS = "RDSCaCert";
	public static final String DEFAULT_PROTOCOL = "TLS";

	public SSLContext buildSSLContext() throws Exception {
		try (InputStream inputStream = getClass().getResourceAsStream(
				ApplicationConstants.RDS_COMBINED_CA_BUNDLE_PATH)) {
			TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
					TrustManagerFactory.getDefaultAlgorithm());
			KeyStore keyStore = buildKeyStore(inputStream);
			trustManagerFactory.init(keyStore);

			KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
					KeyManagerFactory.getDefaultAlgorithm());
			keyManagerFactory.init(keyStore, null);

			SSLContext sslContext = SSLContext.getInstance(DEFAULT_PROTOCOL);
			sslContext.init(
					keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
			log.info("{} | buildSSLContext | sslContext creation success=true : {}, ",
					ApplicationConstants.SCHEDULER_LOG_IDENTIFIER, sslContext.getDefaultSSLParameters());
			return sslContext;
		} catch (MongoClientException | KeyManagementException | IOException | NoSuchAlgorithmException |
				 CertificateException | KeyStoreException | UnrecoverableKeyException e) {
			throw e;
		}
	}

	private KeyStore buildKeyStore(
			InputStream inputStream) throws KeyStoreException, CertificateException, IOException,
			NoSuchAlgorithmException {
		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		keyStore.load(null);
		Iterator<X509Certificate> certIterator = (Iterator<X509Certificate>) CertificateFactory.getInstance(
				DEFAULT_CERT_FORMAT).generateCertificates(inputStream).iterator();
		int certCounter = 0;
		while (certIterator.hasNext()) {
			keyStore.setCertificateEntry(DEFAULT_CERT_ALIAS + certCounter, certIterator.next());
			certCounter += 1;
		}
		return keyStore;
	}
}