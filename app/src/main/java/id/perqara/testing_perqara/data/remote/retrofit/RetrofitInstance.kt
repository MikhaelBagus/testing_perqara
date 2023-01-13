package id.perqara.testing_perqara.data.remote.retrofit

class RetrofitInstance {

    fun createRetrofitInstance(context: Context, baseUrl: String): Retrofit {
        val loggingInterceptor =
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        val accessTokenInterceptor = AccessTokenInterceptor()
        val headerInterceptor = HeaderInterceptor()
        val sslContext = createSslContext(context)
        val systemDefaultTrustManager = createSystemDefaultTrustManager()
        var clientBuilder =
            OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(accessTokenInterceptor)
                .addInterceptor(headerInterceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
        if (sslContext != null && systemDefaultTrustManager != null) {
            clientBuilder =
                clientBuilder.sslSocketFactory(sslContext.socketFactory, systemDefaultTrustManager)
        }
        val gson = GsonBuilder()
            .setLenient()
            .create()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(clientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    fun createRetrofitInstanceWithMembershipUrl(context: Context, premiumUrl: String, freeUrl: String): Retrofit {
        val loggingInterceptor =
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        val accessTokenInterceptor = AccessTokenInterceptor()
        val headerInterceptor = HeaderInterceptor()
        val baseUrlInterceptor = BaseUrlInterceptorConcrete(premiumUrl, freeUrl)
        val sslContext = createSslContext(context)
        val systemDefaultTrustManager = createSystemDefaultTrustManager()
        var clientBuilder =
            OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(accessTokenInterceptor)
                .addInterceptor(headerInterceptor)
                .addInterceptor(baseUrlInterceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
        if (sslContext != null && systemDefaultTrustManager != null) {
            clientBuilder =
                clientBuilder.sslSocketFactory(sslContext.socketFactory, systemDefaultTrustManager)
        }
        val gson = GsonBuilder()
            .setLenient()
            .create()
        return Retrofit.Builder()
            .baseUrl(freeUrl)
            .client(clientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    fun createRetrofitInstanceWithoutCertificate(context: Context, baseUrl: String): Retrofit {
        val loggingInterceptor =
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        val accessTokenInterceptor = AccessTokenInterceptor()
        val headerInterceptor = HeaderInterceptor()
        val clientBuilder =
            OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(accessTokenInterceptor)
                .addInterceptor(headerInterceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)

        val gson = GsonBuilder()
            .setLenient()
            .create()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(clientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    private fun createSslContext(context: Context): SSLContext? {
        var sslContext: SSLContext? = null
        try {
            sslContext = if (BuildConfig.FLAVOR == "development") {
                createCertificate(context.resources.openRawResource(R.raw.certificate_dev))
            } else {
                createCertificate(context.resources.openRawResource(R.raw.certificate))
            }
        } catch (e: CertificateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: KeyStoreException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return sslContext
    }

    @Throws(
        CertificateException::class,
        IOException::class,
        KeyStoreException::class,
        KeyManagementException::class,
        NoSuchAlgorithmException::class
    )
    private fun createCertificate(trustedCertificateIS: InputStream): SSLContext? {
        val cf = CertificateFactory.getInstance("X.509")
        val ca: Certificate = try {
            cf.generateCertificate(trustedCertificateIS)
        } finally {
            trustedCertificateIS.close()
        }

        // creating a KeyStore containing our trusted CAs
        val keyStoreType = KeyStore.getDefaultType()
        val keyStore = KeyStore.getInstance(keyStoreType)
        keyStore.load(null, null)
        keyStore.setCertificateEntry("ca", ca)

        // creating a TrustManager that trusts the CAs in our KeyStore
        val tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm()
        val tmf = TrustManagerFactory.getInstance(tmfAlgorithm)
        tmf.init(keyStore)

        // creating an SSLSocketFactory that uses our TrustManager
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, tmf.trustManagers, null)
        return sslContext
    }

    private fun createSystemDefaultTrustManager(): X509TrustManager? {
        return try {
            val trustManagerFactory =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(null as KeyStore?)
            val trustManagers = trustManagerFactory.trustManagers
            check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
                "Unexpected default trust managers:" + Arrays.toString(
                    trustManagers
                )
            }
            trustManagers[0] as X509TrustManager
        } catch (e: GeneralSecurityException) {
            throw AssertionError() // The system has no TLS. Just give up.
        }
    }

    fun createRetrofitInstanceGateway(context: Context, baseUrl: String): Retrofit {
        val loggingInterceptor =
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        val accessTokenInterceptor = AccessTokenInterceptor()
        val headerInterceptor = HeaderInterceptor()
        val sslContext = createSslContextGateway(context)
        val systemDefaultTrustManager = createSystemDefaultTrustManager()
        var clientBuilder =
            OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(accessTokenInterceptor)
                .addInterceptor(headerInterceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
        if (sslContext != null && systemDefaultTrustManager != null) {
            clientBuilder =
                clientBuilder.sslSocketFactory(sslContext.socketFactory, systemDefaultTrustManager)
        }
        val gson = GsonBuilder()
            .setLenient()
            .create()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(clientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    private fun createSslContextGateway(context: Context): SSLContext? {
        var sslContext: SSLContext? = null
        try {
            sslContext = if (BuildConfig.FLAVOR == "development") {
                createCertificate(context.resources.openRawResource(R.raw.certificate))
            } else {
                createCertificate(context.resources.openRawResource(R.raw.certificate))
            }
        } catch (e: CertificateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: KeyStoreException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return sslContext
    }
}