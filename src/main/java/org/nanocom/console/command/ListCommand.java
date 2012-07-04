package org.nanocom.console.command;

import java.util.Arrays;

import org.nanocom.console.input.InputArgument;
import org.nanocom.console.input.InputDefinition;
import org.nanocom.console.input.InputInterface;
import org.nanocom.console.input.InputOption;
import org.nanocom.console.output.OutputInterface;

/**
 * ListCommand displays the list of all available commands for the application.
 *
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class ListCommand extends Command {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configure() {
        setName("list");
        setDefinition(createDefinition());
        setDescription("Lists commands");
        setHelp("The <info>list</info> command lists all commands:"
            + "<info>php app/console list</info>"
            + "You can also display the commands for a specific namespace:"
            + "<info>php app/console list test</info>");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected InputDefinition getNativeDefinition() {
        return createDefinition();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int execute(InputInterface input, OutputInterface output) {
        output.writeln(getApplication().asText((String) input.getArgument("namespace"), false));

        return 0;
    }

    private InputDefinition createDefinition() {
        return new InputDefinition(Arrays.<Object>asList(
            new InputArgument("namespace", InputArgument.OPTIONAL, "The namespace name")
        ));
    }
}
