package main;


class CommandBuilder
{
    private StringBuilder command;

    CommandBuilder()
    {
        command = new StringBuilder();
    }

    void addCommand(String code)
    {
        if (command.toString().isEmpty())
        {
            command.append(code);
        } else
        {
            command.append("@");
            command.append(code);
        }
    }

    void addCommand(String code, String data)
    {
        if (command.toString().isEmpty())
        {
            command.append(code);
            command.append(" ");
            command.append(data);
        } else
        {
            command.append("@");
            command.append(code);
            command.append(" ");
            command.append(data);
        }

    }


    String getCommand()
    {
        return command.toString();
    }
}
