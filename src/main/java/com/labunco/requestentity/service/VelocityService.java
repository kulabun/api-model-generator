package com.labunco.requestentity.service;

import com.labunco.requestentity.model.Clazz;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.util.StringUtils;

import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.List;
import java.util.Properties;

/**
 * @author kulabun
 * @since 3/25/17
 */
public class VelocityService {
    private VelocityEngine engine;
    private Template requestEntityTemplate;

    public VelocityService() {
        engine = new VelocityEngine(readProperties());
        requestEntityTemplate = engine.getTemplate("templates/request-entity.vm", "UTF-8");
        engine.init();
    }

    public void writeRequestEntity(Writer writer, List<String> imports, Clazz clazz) {
        VelocityContext ctx = new VelocityContext();
        ctx.put("imports", imports);
        ctx.put("clazz", clazz);
        ctx.put("StringUtils", StringUtils.class);
        requestEntityTemplate.merge(ctx, writer);
    }

    private Properties readProperties() {
        try {
            URL url = this.getClass().getClassLoader().getResource("conf/velocity.properties");
            Properties properties = new Properties();
            properties.load(url.openStream());
            return properties;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
