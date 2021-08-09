/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2021 Jon Brule <brulejr@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.jrb.labs.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Properties;

import static java.lang.String.format;

@Slf4j
public class DockerSecretsProcessor implements EnvironmentPostProcessor {

    private static final String SECRET_PATH = "/run/secrets/%s";

    @Override
    public void postProcessEnvironment(final ConfigurableEnvironment environment, final SpringApplication application) {
        transferSecret("KEYCLOAK_CLIENT_ID_FILE", "KEYCLOAK_CLIENT_ID", environment);
        transferSecret("KEYCLOAK_CLIENT_SECRET_FILE", "KEYCLOAK_CLIENT_SECRET", environment);
    }

    private void transferSecret(final String secretName, final String propertyName, final ConfigurableEnvironment environment) {
        final String secretPath = environment.getProperty(secretName, format(SECRET_PATH, secretName));
        final Resource resource = new FileSystemResource(secretPath);
        if (resource.exists()) {
            try {
                log.info("Injecting secret '{}' into property '{}'...", secretName, propertyName);
                final String secretValue = StreamUtils.copyToString(resource.getInputStream(), Charset.defaultCharset());

                final Properties props = new Properties();
                props.put(propertyName, secretValue);
                environment.getPropertySources().addLast(new PropertiesPropertySource("dockerSecrets", props));

            } catch(final IOException e) {
                log.error("Unable to read secret '{}' from disk", secretName, e);
                throw new IllegalStateException(e);
            }
        }
    }

}
