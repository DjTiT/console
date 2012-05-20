package com.nanocom.console;

import com.nanocom.console.command.Command;
import com.nanocom.console.command.HelpCommand;
import com.nanocom.console.fixtures.Foo1Command;
import com.nanocom.console.fixtures.FooCommand;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class ApplicationTest {

    public ApplicationTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    protected void normalizeLineBreaks(final String text) {
        text.replaceAll(System.getProperty("line.separator"), "\n");
    }

    /**
     * Replaces the dynamic placeholders of the command help text with a static version.
     * The placeholder %command.full_name% includes the script path that is not predictable
     * and can not be tested against.
     */
    protected void ensureStaticCommandHelp(Application application) {
        for (Entry<String, Command> command : application.all().entrySet()) {
            // command.setHelp(command.getHelp().replaceAll("%command.full_name%", "app/console %command.name%"));
        }
    }

    @Test
    public void testConstructor() throws Exception {
        Application application = new Application("foo", "bar");
        assertEquals("__construct() takes the application name as its first argument", "foo", application.getName());
        assertEquals("__construct() takes the application version as its first argument", "bar", application.getVersion());
        assertTrue("__construct() registered the help and list commands by default", application.all().containsKey("help") && application.all().containsKey("list"));
    }

    @Test
    public void testSetGetName() throws Exception {
        Application application = new Application();
        application.setName("foo");
        assertEquals(".setName() sets the name of the application", "foo", application.getName());
    }

    @Test
    public void testSetGetVersion() throws Exception {
        Application application = new Application();
        application.setVersion("bar");
        assertEquals(".setVersion() sets the version of the application", "bar", application.getVersion());
    }

    @Test
    public void testGetLongVersion() throws Exception {
        Application application = new Application("foo", "bar");
        //assertEquals(".getLongVersion() returns the long version of the application", "<info>foo</info> version <comment>bar</comment>", application.getLongVersion());
    }

    @Test
    public void testHelp() throws Exception {
        Application application = new Application();
        // assertStringEqualsFile(".setHelp() returns a help message", self.fixturesPath."/application_gethelp.txt", this.normalizeLineBreaks(application.getHelp()));
    }

    @Test
    public void testAll() throws Exception {
        Application application = new Application();
        Map<String, Command> commands = application.all();
        assertEquals(".all() returns the registered commands", HelpCommand.class, commands.get("help").getClass());

        application.add(new FooCommand());
        commands = application.all("foo");
        assertEquals(".all() takes a namespace as its first argument", 1, commands.size());
    }

    @Test
    public void testRegister() throws Exception {
        Application application = new Application();
        Command command = application.register("foo");
        assertEquals(".register() registers a new command", "foo", command.getName());
    }

    @Test
    public void testAdd() throws Exception {
        Application application = new Application();
        Command foo = new FooCommand();
        application.add(foo);
        Map<String, Command> commands = application.all();
        assertEquals(".add() registers a command", foo, commands.get("foo:bar"));

        application = new Application();
        foo = new FooCommand();
        Command foo1 = new Foo1Command();
        application.addCommands(Arrays.asList(foo, foo1));
        commands = application.all();
        assertEquals(".addCommands() registers an array of commands", Arrays.asList(foo, foo1), Arrays.asList(commands.get("foo:bar"), commands.get("foo:bar1")));
    }

    /*public void testHasGet() throws Exception {
        Application application = new Application();
        assertTrue(application.has("list"), ".has() returns true if a named command is registered");
        assertFalse(application.has("afoobar"), ".has() returns false if a named command is not registered");

        application.add(foo = new FooCommand());
        assertTrue(application.has("afoobar"), ".has() returns true if an alias is registered");
        assertEquals(foo, application.get("foo:bar"), ".get() returns a command by name");
        assertEquals(foo, application.get("afoobar"), ".get() returns a command by alias");

        try {
            application.get("foofoo");
            fail(".get() throws an \InvalidArgumentException if the command does not exist");
        } .catch (Exception e) {
            assertInstanceOf("\InvalidArgumentException", e, ".get() throws an \InvalidArgumentException if the command does not exist");
            assertEquals("The command \"foofoo\" does not exist.", e.getMessage(), ".get() throws an \InvalidArgumentException if the command does not exist");
        }

        application = new Application();
        application.add(foo = new \FooCommand());
        // simulate --help
        r = new \ReflectionObject(application);
        p = r.getProperty("wantHelps");
        p.setAccessible(true);
        p.setValue(application, true);
        command = application.get("foo:bar");
        assertEquals("Symfony\Component\Console\Command\HelpCommand", get_class(command), ".get() returns the help command if --help is provided as the input");
    }

    public void testGetNamespaces() {
        Application application = new Application();
        application.add(new \FooCommand());
        application.add(new \Foo1Command());
        assertEquals(array("foo"), application.getNamespaces(), ".getNamespaces() returns an array of unique used namespaces");
    }

    public void testFindNamespace() {
        Application application = new Application();
        application.add(new \FooCommand());
        assertEquals("foo", application.findNamespace("foo"), ".findNamespace() returns the given namespace if it exists");
        assertEquals("foo", application.findNamespace("f"), ".findNamespace() finds a namespace given an abbreviation");
        application.add(new \Foo2Command());
        assertEquals("foo", application.findNamespace("foo"), ".findNamespace() returns the given namespace if it exists");
        try {
            application.findNamespace("f");
            fail(".findNamespace() throws an \InvalidArgumentException if the abbreviation is ambiguous");
        } .catch (Exception e) {
            assertInstanceOf("\InvalidArgumentException", e, ".findNamespace() throws an \InvalidArgumentException if the abbreviation is ambiguous");
            assertEquals("The namespace \"f\" is ambiguous (foo, foo1).", e.getMessage(), ".findNamespace() throws an \InvalidArgumentException if the abbreviation is ambiguous");
        }

        try {
            application.findNamespace("bar");
            fail(".findNamespace() throws an \InvalidArgumentException if no command is in the given namespace");
        } .catch (Exception e) {
            assertInstanceOf("\InvalidArgumentException", e, ".findNamespace() throws an \InvalidArgumentException if no command is in the given namespace");
            assertEquals("There are no commands defined in the \"bar\" namespace.", e.getMessage(), ".findNamespace() throws an \InvalidArgumentException if no command is in the given namespace");
        }
    }

    public void testFind() {
        Application application = new Application();
        application.add(new \FooCommand());
        assertEquals("FooCommand", get_class(application.find("foo:bar")), ".find() returns a command if its name exists");
        assertEquals("Symfony\Component\Console\Command\HelpCommand", get_class(application.find("h")), ".find() returns a command if its name exists");
        assertEquals("FooCommand", get_class(application.find("f:bar")), ".find() returns a command if the abbreviation for the namespace exists");
        assertEquals("FooCommand", get_class(application.find("f:b")), ".find() returns a command if the abbreviation for the namespace and the command name exist");
        assertEquals("FooCommand", get_class(application.find("a")), ".find() returns a command if the abbreviation exists for an alias");

        application.add(new \Foo1Command());
        application.add(new \Foo2Command());

        try {
            application.find("f");
            fail(".find() throws an \InvalidArgumentException if the abbreviation is ambiguous for a namespace");
        } catch (Exception e) {
            assertInstanceOf("\InvalidArgumentException", e, ".find() throws an \InvalidArgumentException if the abbreviation is ambiguous for a namespace");
            assertRegExp("/Command \"f\" is not defined./", e.getMessage(), ".find() throws an \InvalidArgumentException if the abbreviation is ambiguous for a namespace");
        }

        try {
            application.find("a");
            fail(".find() throws an \InvalidArgumentException if the abbreviation is ambiguous for an alias");
        } catch (Exception e) {
            assertInstanceOf("\InvalidArgumentException", e, ".find() throws an \InvalidArgumentException if the abbreviation is ambiguous for an alias");
            assertEquals("Command \"a\" is ambiguous (afoobar, afoobar1 and 1 more).", e.getMessage(), ".find() throws an \InvalidArgumentException if the abbreviation is ambiguous for an alias");
        }

        try {
            application.find("foo:b");
            fail(".find() throws an \InvalidArgumentException if the abbreviation is ambiguous for a command");
        } catch (Exception e) {
            assertInstanceOf("\InvalidArgumentException", e, ".find() throws an \InvalidArgumentException if the abbreviation is ambiguous for a command");
            assertEquals("Command \"foo:b\" is ambiguous (foo:bar, foo:bar1).", e.getMessage(), ".find() throws an \InvalidArgumentException if the abbreviation is ambiguous for a command");
        }
    }

    public void testFindAlternativeCommands() {
        Application application = new Application();

        application.add(new \FooCommand());
        application.add(new \Foo1Command());
        application.add(new \Foo2Command());

        try {
            application.find(commandName = "Unknow command");
            fail(".find() throws an \InvalidArgumentException if command does not exist");
        } catch (Exception e) {
            assertInstanceOf("\InvalidArgumentException", e, ".find() throws an \InvalidArgumentException if command does not exist");
            assertEquals(sprintf("Command \"%s\" is not defined.", commandName), e.getMessage(), ".find() throws an \InvalidArgumentException if command does not exist, without alternatives");
        }

        try {
            application.find(commandName = "foo");
            fail(".find() throws an \InvalidArgumentException if command does not exist");
        } catch (Exception e) {
            assertInstanceOf("\InvalidArgumentException", e, ".find() throws an \InvalidArgumentException if command does not exist");
            assertRegExp(sprintf("/Command \"%s\" is not defined./", commandName), e.getMessage(), ".find() throws an \InvalidArgumentException if command does not exist, with alternatives");
            assertRegExp("/foo:bar/", e.getMessage(), ".find() throws an \InvalidArgumentException if command does not exist, with alternative : \"foo:bar\"");
            assertRegExp("/foo1:bar/", e.getMessage(), ".find() throws an \InvalidArgumentException if command does not exist, with alternative : \"foo1:bar\"");
            assertRegExp("/foo:bar1/", e.getMessage(), ".find() throws an \InvalidArgumentException if command does not exist, with alternative : \"foo:bar1\"");
        }

        // Test if \"foo1\" command throw an \"\InvalidArgumentException\" and does not contain
        // \"foo:bar\" as alternative because \"foo1\" is too far from \"foo:bar\"
        try {
            application.find(commandName = "foo1");
            fail(".find() throws an \InvalidArgumentException if command does not exist");
        } catch (Exception e) {
            assertInstanceOf("\InvalidArgumentException", e, ".find() throws an \InvalidArgumentException if command does not exist");
            assertRegExp(sprintf("/Command \"%s\" is not defined./", commandName), e.getMessage(), ".find() throws an \InvalidArgumentException if command does not exist, with alternatives");
            assertFalse(strpos(e.getMessage(), "foo:bar"), ".find() throws an \InvalidArgumentException if command does not exist, without \"foo:bar\" alternative");
        }
    }

    public void testFindAlternativeNamespace() {
        Application application = new Application();

        application.add(new \FooCommand());
        application.add(new \Foo1Command());
        application.add(new \Foo2Command());
        application.add(new \foo3Command());

        try {
            application.find("Unknow-namespace:Unknow-command");
            fail(".find() throws an \InvalidArgumentException if namespace does not exist");
        } catch (Exception e) {
            assertInstanceOf("\InvalidArgumentException", e, ".find() throws an \InvalidArgumentException if namespace does not exist");
            assertEquals("There are no commands defined in the \"Unknow-namespace\" namespace.", e.getMessage(), ".find() throws an \InvalidArgumentException if namespace does not exist, without alternatives");
        }

        try {
            application.find("foo2:command");
            fail(".find() throws an \InvalidArgumentException if namespace does not exist");
        } catch (Exception e) {
            assertInstanceOf("\InvalidArgumentException", e, ".find() throws an \InvalidArgumentException if namespace does not exist");
            assertRegExp("/There are no commands defined in the \"foo2\" namespace./", e.getMessage(), ".find() throws an \InvalidArgumentException if namespace does not exist, with alternative");
            assertRegExp("/foo/", e.getMessage(), ".find() throws an \InvalidArgumentException if namespace does not exist, with alternative : \"foo\"");
            assertRegExp("/foo1/", e.getMessage(), ".find() throws an \InvalidArgumentException if namespace does not exist, with alternative : \"foo1\"");
            assertRegExp("/foo3/", e.getMessage(), ".find() throws an \InvalidArgumentException if namespace does not exist, with alternative : \"foo3\"");
        }
    }

    public void testSetCatchExceptions() {
        Application application = this.getMock("Symfony\Component\Console\Application", array("getTerminalWidth"));
        application.setAutoExit(false);
        application.expects(this.any())
            .method("getTerminalWidth")
            .will(this.returnValue(120));
        tester = new ApplicationTester(application);

        application.setCatchExceptions(true);
        tester.run(array("command" => "foo"), array("decorated" => false));
        assertStringEqualsFile(self.fixturesPath."/application_renderexception1.txt", this.normalizeLineBreaks(tester.getDisplay()), ".setCatchExceptions() sets the catch exception flag");

        application.setCatchExceptions(false);
        try {
            tester.run(array("command" => "foo"), array("decorated" => false));
            fail(".setCatchExceptions() sets the catch exception flag");
        } catch (Exception e) {
            assertInstanceOf("\Exception", e, ".setCatchExceptions() sets the catch exception flag");
            assertEquals("Command \"foo\" is not defined.", e.getMessage(), ".setCatchExceptions() sets the catch exception flag");
        }
    }

    public void testAsText() throws Exception {
        Application application = new Application();
        application.add(new FooCommand());
        this.ensureStaticCommandHelp(application);
        assertStringEqualsFile(self.fixturesPath."/application_astext1.txt", this.normalizeLineBreaks(application.asText()), ".asText() returns a text representation of the application");
        assertStringEqualsFile(self.fixturesPath."/application_astext2.txt", this.normalizeLineBreaks(application.asText("foo")), ".asText() returns a text representation of the application");
    }

    public void testAsXml() throws Exception {
        Application application = new Application();
        application.add(new FooCommand());
        this.ensureStaticCommandHelp(application);
        assertXmlStringEqualsXmlFile(self.fixturesPath."/application_asxml1.txt", application.asXml(), ".asXml() returns an XML representation of the application");
        assertXmlStringEqualsXmlFile(self.fixturesPath."/application_asxml2.txt", application.asXml("foo"), ".asXml() returns an XML representation of the application");
    }

    public void testRenderException() {
        Application application = this.getMock("Symfony\Component\Console\Application", array("getTerminalWidth"));
        application.setAutoExit(false);
        application.expects(this.any())
            .method("getTerminalWidth")
            .will(this.returnValue(120));
        tester = new ApplicationTester(application);

        tester.run(array("command" => "foo"), array("decorated" => false));
        assertStringEqualsFile(self.fixturesPath."/application_renderexception1.txt", this.normalizeLineBreaks(tester.getDisplay()), ".renderException() renders a pretty exception");

        tester.run(array("command" => "foo"), array("decorated" => false, "verbosity" => Output.VERBOSITY_VERBOSE));
        assertContains("Exception trace", tester.getDisplay(), ".renderException() renders a pretty exception with a stack trace when verbosity is verbose");

        tester.run(array("command" => "list", "--foo" => true), array("decorated" => false));
        assertStringEqualsFile(self.fixturesPath."/application_renderexception2.txt", this.normalizeLineBreaks(tester.getDisplay()), ".renderException() renders the command synopsis when an exception occurs in the context of a command");

        application.add(new \Foo3Command);
        tester = new ApplicationTester(application);
        tester.run(array("command" => "foo3:bar"), array("decorated" => false));
        assertStringEqualsFile(self.fixturesPath."/application_renderexception3.txt", this.normalizeLineBreaks(tester.getDisplay()), ".renderException() renders a pretty exceptions with previous exceptions");

        application = this.getMock("Symfony\Component\Console\Application", array("getTerminalWidth"));
        application.setAutoExit(false);
        application.expects(this.any())
            .method("getTerminalWidth")
            .will(this.returnValue(32));
        tester = new ApplicationTester(application);

        application = this.getMock("Symfony\Component\Console\Application", array("getTerminalWidth"));
        application.setAutoExit(false);
        application.expects(this.any())
            .method("getTerminalWidth")
            .will(this.returnValue(32));
        tester = new ApplicationTester(application);

        tester.run(array("command" => "foo"), array("decorated" => false));
        assertStringEqualsFile(self.fixturesPath."/application_renderexception4.txt", this.normalizeLineBreaks(tester.getDisplay()), ".renderException() wraps messages when they are bigger than the terminal");
    }

    public void testRun() throws Exception {
        Application application = new Application();
        application.setAutoExit(false);
        application.setCatchExceptions(false);
        application.add(command = new \Foo1Command());
        _SERVER["argv"] = array("cli.php", "foo:bar1");

        ob_start();
        application.run();
        ob_end_clean();

        assertSame("Symfony\Component\Console\Input\ArgvInput", get_class(command.input), ".run() creates an ArgvInput by default if none is given");
        assertSame("Symfony\Component\Console\Output\ConsoleOutput", get_class(command.output), ".run() creates a ConsoleOutput by default if none is given");

        application = new Application();
        application.setAutoExit(false);
        application.setCatchExceptions(false);

        this.ensureStaticCommandHelp(application);
        tester = new ApplicationTester(application);

        tester.run(array(), array("decorated" => false));
        assertStringEqualsFile(self.fixturesPath."/application_run1.txt", this.normalizeLineBreaks(tester.getDisplay()), ".run() runs the list command if no argument is passed");

        tester.run(array("--help" => true), array("decorated" => false));
        assertStringEqualsFile(self.fixturesPath."/application_run2.txt", this.normalizeLineBreaks(tester.getDisplay()), ".run() runs the help command if --help is passed");

        tester.run(array("-h" => true), array("decorated" => false));
        assertStringEqualsFile(self.fixturesPath."/application_run2.txt", this.normalizeLineBreaks(tester.getDisplay()), ".run() runs the help command if -h is passed");

        tester.run(array("command" => "list", "--help" => true), array("decorated" => false));
        assertStringEqualsFile(self.fixturesPath."/application_run3.txt", this.normalizeLineBreaks(tester.getDisplay()), ".run() displays the help if --help is passed");

        tester.run(array("command" => "list", "-h" => true), array("decorated" => false));
        assertStringEqualsFile(self.fixturesPath."/application_run3.txt", this.normalizeLineBreaks(tester.getDisplay()), ".run() displays the help if -h is passed");

        tester.run(array("--ansi" => true));
        assertTrue(tester.getOutput().isDecorated(), ".run() forces color output if --ansi is passed");

        tester.run(array("--no-ansi" => true));
        assertFalse(tester.getOutput().isDecorated(), ".run() forces color output to be disabled if --no-ansi is passed");

        tester.run(array("--version" => true), array("decorated" => false));
        assertStringEqualsFile(self.fixturesPath."/application_run4.txt", this.normalizeLineBreaks(tester.getDisplay()), ".run() displays the program version if --version is passed");

        tester.run(array("-V" => true), array("decorated" => false));
        assertStringEqualsFile(self.fixturesPath."/application_run4.txt", this.normalizeLineBreaks(tester.getDisplay()), ".run() displays the program version if -v is passed");

        tester.run(array("command" => "list", "--quiet" => true));
        assertSame("", tester.getDisplay(), ".run() removes all output if --quiet is passed");

        tester.run(array("command" => "list", "-q" => true));
        assertSame("", tester.getDisplay(), ".run() removes all output if -q is passed");

        tester.run(array("command" => "list", "--verbose" => true));
        assertSame(Output.VERBOSITY_VERBOSE, tester.getOutput().getVerbosity(), ".run() sets the output to verbose if --verbose is passed");

        tester.run(array("command" => "list", "-v" => true));
        assertSame(Output.VERBOSITY_VERBOSE, tester.getOutput().getVerbosity(), ".run() sets the output to verbose if -v is passed");

        application = new Application();
        application.setAutoExit(false);
        application.setCatchExceptions(false);
        application.add(new \FooCommand());
        tester = new ApplicationTester(application);

        tester.run(array("command" => "foo:bar", "--no-interaction" => true), array("decorated" => false));
        assertSame("called".PHP_EOL, tester.getDisplay(), ".run() does not call interact() if --no-interaction is passed");

        tester.run(array("command" => "foo:bar", "-n" => true), array("decorated" => false));
        assertSame("called".PHP_EOL, tester.getDisplay(), ".run() does not call interact() if -n is passed");
    }*/

    /**
     * @expectedException \LogicException
     * @dataProvider getAddingAlreadySetDefinitionElementData
     */
    /*public void testAddingAlreadySetDefinitionElementData(def) throws Exception {
        Application application = new Application();
        application.setAutoExit(false);
        application.setCatchExceptions(false);
        application
            .register("foo")
            .setDefinition(array(def))
            .setCode(void (InputInterface input, OutputInterface output) {})
        ;

        input = new ArrayInput(array("command" => "foo"));
        output = new NullOutput();
        application.run(input, output);
    }

    public List<Object> getAddingAlreadySetDefinitionElementData() {
        return Arrays.asList(
            array(new InputArgument("command", InputArgument.REQUIRED)),
            array(new InputOption("quiet", "", InputOption.VALUE_NONE)),
            array(new InputOption("query", "q", InputOption.VALUE_NONE)),
        );
    }*/

}