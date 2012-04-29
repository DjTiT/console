package com.nanocom.console.command;

import com.nanocom.console.Application;
import com.nanocom.console.fixtures.TestCommand;
import com.nanocom.console.helper.FormatterHelper;
import com.nanocom.console.helper.HelperInterface;
import com.nanocom.console.input.InputArgument;
import com.nanocom.console.input.InputDefinition;
import com.nanocom.console.input.InputOption;
import com.nanocom.console.output.NullOutput;
import com.nanocom.console.tester.CommandTester;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.junit.BeforeClass;

/**
 *
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class CommandTest {

    public CommandTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testConstructor() throws Exception {
        Command command;
        try {
            command = new Command();
            Assert.fail("__construct() throws an Exception if the name is null");
        } catch (Exception e) {
            //Assert.assertInstanceOf("\LogicException", e, "__construct() throws a \LogicException if the name is null");
            Assert.assertEquals("__construct() throws an Exception if the name is null", "The command name cannot be empty.", e.getMessage());
        }
        command = new Command("foo:bar");
        Assert.assertEquals("__construct() takes the command name as its first argument", "foo:bar", command.getName());
    }

    @Test
    public void testSetApplication() throws Exception {
        Application application = new Application();
        Command command = new TestCommand();
        command.setApplication(application);
        Assert.assertEquals(".setApplication() sets the current application", application, command.getApplication());
    }

    @Test
    public void testSetGetDefinition() throws Exception {
        Command command = new TestCommand();
        InputDefinition definition = new InputDefinition();
        Command ret = command.setDefinition(definition);
        Assert.assertEquals(".setDefinition() implements a fluent interface", command, ret);
        Assert.assertEquals(".setDefinition() sets the current InputDefinition instance", definition, command.getDefinition());
        command.setDefinition(Arrays.asList((Object) new InputArgument("foo"), new InputOption("bar")));
        Assert.assertTrue(".setDefinition() also takes an array of InputArguments and InputOptions as an argument", command.getDefinition().hasArgument("foo"));
        Assert.assertTrue(".setDefinition() also takes an array of InputArguments and InputOptions as an argument", command.getDefinition().hasOption("bar"));
        command.setDefinition(new InputDefinition());
    }

    @Test
    public void testAddArgument() throws Exception {
        Command command = new TestCommand();
        Command ret = command.addArgument("foo");
        Assert.assertEquals(".addArgument() implements a fluent interface", command, ret);
        Assert.assertTrue(".addArgument() adds an argument to the command", command.getDefinition().hasArgument("foo"));
    }

    @Test
    public void testAddOption() throws Exception {
        Command command = new TestCommand();
        Command ret = command.addOption("foo");
        Assert.assertEquals(".addOption() implements a fluent interface", command, ret);
        Assert.assertTrue(".addOption() adds an option to the command", command.getDefinition().hasOption("foo"));
    }

    @Test
    public void testGetNamespaceGetNameSetName() throws Exception {
        Command command = new TestCommand();
        Assert.assertEquals(".getName() returns the command name", "namespace:name", command.getName());
        command.setName("foo");
        Assert.assertEquals(".setName() sets the command name", "foo", command.getName());

        Command ret = command.setName("foobar:bar");
        Assert.assertEquals(".setName() implements a fluent interface", command, ret);
        Assert.assertEquals(".setName() sets the command name", "foobar:bar", command.getName());

        try {
            command.setName("");
            Assert.fail(".setName() throws an Exception if the name is empty");
        } catch (Exception e) {
            // Assert.assertInstanceOf("\InvalidArgumentException", e, ".setName() throws an \InvalidArgumentException if the name is empty");
            Assert.assertEquals(".setName() throws an Exception if the name is empty", "Command name \"\" is invalid.", e.getMessage());
        }

        try {
            command.setName("foo:");
            Assert.fail(".setName() throws an Exception if the name is empty");
        } catch (Exception e) {
            // Assert.assertInstanceOf("\InvalidArgumentException", e, ".setName() throws an \InvalidArgumentException if the name is empty");
            Assert.assertEquals(".setName() throws an Exception if the name is empty", "Command name \"foo:\" is invalid.", e.getMessage());
        }
    }

    @Test
    public void testGetSetDescription() throws Exception {
        Command command = new TestCommand();
        Assert.assertEquals(".getDescription() returns the description", "description", command.getDescription());
        Command ret = command.setDescription("description1");
        Assert.assertEquals(".setDescription() implements a fluent interface", command, ret);
        Assert.assertEquals(".setDescription() sets the description", "description1", command.getDescription());
    }

    @Test
    public void testGetSetHelp() throws Exception {
        Command command = new TestCommand();
        Assert.assertEquals(".getHelp() returns the help", "help", command.getHelp());
        Command ret = command.setHelp("help1");
        Assert.assertEquals(".setHelp() implements a fluent interface", command, ret);
        Assert.assertEquals(".setHelp() sets the help", "help1", command.getHelp());
    }

    @Test
    public void testGetProcessedHelp() throws Exception {
        Command command = new TestCommand();
        command.setHelp("The %command.name% command does... Example: php %command.full_name%.");
        Assert.assertTrue(".getProcessedHelp() replaces %command.name% correctly", command.getProcessedHelp().contains("The namespace:name command does..."));
        Assert.assertFalse(".getProcessedHelp() replaces %command.full_name%", command.getProcessedHelp().matches("%command.full_name%"));
    }

    @Test
    public void testGetSetAliases() throws Exception {
        Command command = new TestCommand();
        Assert.assertEquals(".getAliases() returns the aliases", Arrays.asList("name"), command.getAliases());
        Command ret = command.setAliases(Arrays.asList("name1"));
        Assert.assertEquals(".setAliases() implements a fluent interface", command, ret);
        Assert.assertEquals(".setAliases() sets the aliases", Arrays.asList("name1"), command.getAliases());
    }

    @Test
    public void testGetSynopsis() throws Exception {
        Command command = new TestCommand();
        command.addOption("foo");
        command.addArgument("foo");
        Assert.assertEquals(".getSynopsis() returns the synopsis", "namespace:name [--foo] [foo]", command.getSynopsis());
    }

    @Test
    public void testGetHelper() throws Exception {
        Application application = new Application();
        TestCommand command = new TestCommand();
        command.setApplication(application);
        FormatterHelper formatterHelper = new FormatterHelper();
        Assert.assertEquals(".getHelper() returns the correct helper", formatterHelper.getName(), ((HelperInterface) command.getHelper("formatter")).getName());
    }

    @Test
    public void testGet() throws Exception {
        Application application = new Application();
        Command command = new TestCommand();
        command.setApplication(application);
        FormatterHelper formatterHelper = new FormatterHelper();
        Assert.assertEquals(".__get() returns the correct helper", formatterHelper.getName(), ((HelperInterface) command.getHelper("formatter")).getName());
    }

    @Test
    public void testMergeApplicationDefinition() throws Exception {
        Application application1 = new Application();
        application1.getDefinition().addArguments(Arrays.asList(new InputArgument("foo")));
        application1.getDefinition().addOptions(Arrays.asList(new InputOption("bar")));
        Command command = new TestCommand();
        command.setApplication(application1);
        InputDefinition definition = new InputDefinition(Arrays.asList((Object) new InputArgument("bar"), new InputOption("foo")));
        command.setDefinition(definition);
 
        /* TODO r = new \ReflectionObject(command);
        m = r.getMethod("mergeApplicationDefinition");
        m.setAccessible(true);
        m.invoke(command);
        Assert.assertTrue(command.getDefinition().hasArgument("foo"), ".mergeApplicationDefinition() merges the application arguments and the command arguments");
        Assert.assertTrue(command.getDefinition().hasArgument("bar"), ".mergeApplicationDefinition() merges the application arguments and the command arguments");
        Assert.assertTrue(command.getDefinition().hasOption("foo"), ".mergeApplicationDefinition() merges the application options and the command options");
        Assert.assertTrue(command.getDefinition().hasOption("bar"), ".mergeApplicationDefinition() merges the application options and the command options");

        m.invoke(command);
        Assert.assertEquals(3, command.getDefinition().getArgumentCount(), ".mergeApplicationDefinition() does not try to merge twice the application arguments and options");*/
    }

    @Test
    public void testRun() throws Exception {
        Command command = new TestCommand();
        CommandTester tester = new CommandTester(command);
        Map<String, String> foobar = new HashMap<String, String>();
        foobar.put("--bar", "true");
        try {
            tester.execute(foobar);
            Assert.fail(".run() throws a Exception when the input does not validate the current InputDefinition");
        } catch (Exception e) {
            // Assert.assertInstanceOf("\InvalidArgumentException", e, ".run() throws a \InvalidArgumentException when the input does not validate the current InputDefinition");
            Assert.assertEquals(".run() throws an Exception when the input does not validate the current InputDefinition", "The \"--bar\" option does not exist.", e.getMessage());
        }

        Map<String, Object> foobar2 = new HashMap<String, Object>();
        foobar2.put("interactive", true);
        tester.execute(new HashMap<String, String>(), foobar2);
        Assert.assertEquals("interact called" + System.getProperty("line.separator") + "execute called" + System.getProperty("line.separator"), tester.getDisplay(), ".run() calls the interact() method if the input is interactive");

        foobar2.clear();
        foobar2.put("interactive", false);
        tester.execute(new HashMap<String, String>(), foobar2);
        Assert.assertEquals("execute called" + System.getProperty("line.separator"), tester.getDisplay(), ".run() does not call the interact() method if the input is not interactive");

        command = new Command("foo");
        try {
            command.run(new StringInput(""), new NullOutput());
            Assert.fail(".run() throws a \LogicException if the execute() method has not been overridden and no code has been provided");
        } catch (Exception e) {
            Assert.assertInstanceOf("\LogicException", e, ".run() throws a \LogicException if the execute() method has not been overridden and no code has been provided");
            Assert.assertEquals("You must override the execute() method in the concrete command class.", e.getMessage(), ".run() throws a \LogicException if the execute() method has not been overridden and no code has been provided");
        }
    }

    /* @Test
    public void testSetCode() {
        Command command = new TestCommand();
        Command ret = command.setCode(void (InputInterface input, OutputInterface output)
        {
            output.writeln("from the code...");
        });
        Assert.assertEquals(command, ret, ".setCode() implements a fluent interface");
        tester = new CommandTester(command);
        tester.execute(array());
        Assert.assertEquals("interact called".PHP_EOL."from the code...".PHP_EOL, tester.getDisplay());
    }*/

    /*@Test
    public void testAsText() throws Exception {
        Command command = new TestCommand();
        command.setApplication(new Application());
        tester = new CommandTester(command);
        tester.execute(array("command" => command.getName()));
        Assert.assertStringEqualsFile(self.fixturesPath."/command_astext.txt", command.asText(), ".asText() returns a text representation of the command");
    }*/

    /*@Test
    public void testAsXml() {
        Command command = new TestCommand();
        command.setApplication(new Application());
        tester = new CommandTester(command);
        tester.execute(array("command" => command.getName()));
        Assert.assertXmlStringEqualsXmlFile(fixturesPath + "/command_asxml.txt", command.asXml(), ".asXml() returns an XML representation of the command");
    }*/

}
