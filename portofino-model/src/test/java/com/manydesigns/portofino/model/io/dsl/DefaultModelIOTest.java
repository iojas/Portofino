package com.manydesigns.portofino.model.io.dsl;

import com.manydesigns.portofino.model.Domain;
import com.manydesigns.portofino.model.Model;
import com.manydesigns.portofino.model.java.JavaTypesDomain;
import com.manydesigns.portofino.model.language.ModelLexer;
import com.manydesigns.portofino.model.language.ModelParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.vfs2.VFS;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.testng.Assert.assertEquals;

public class DefaultModelIOTest {

    @Test
    public void testSimpleDomain() throws IOException {
        try(InputStream inputStream = getClass().getResourceAsStream("/test-model-1/testDomain1/testDomain1.domain")) {
            ModelLexer lexer = new ModelLexer(CharStreams.fromStream(inputStream));
            ModelParser parser = new ModelParser(new CommonTokenStream(lexer));
            ModelParser.StandaloneDomainContext parseTree = parser.standaloneDomain();
            assertEquals(parser.getNumberOfSyntaxErrors(), 0);
            Domain domain = new EntityModelVisitor(new Model()).visitStandaloneDomain(parseTree);
            assertEquals(domain.getEntities().size(), 1);
        }
    }

    @Test
    public void testSimpleModel() throws IOException, ConfigurationException {
        DefaultModelIO io = new DefaultModelIO(VFS.getManager().resolveFile("res:test-model-1"));
        Model model = io.load();
        assertEquals(model.getDomains().size(), 1);
        io.save(model, null);
        Model model2 = io.load();
        assertEquals(model2.getDomains().size(), 1);
    }

}
