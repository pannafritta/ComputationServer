package command;

public class StatCommand extends Command {
    private final String statKind;

    public StatCommand(String statKind) {
        this.statKind = statKind;
    }

    @Override
    public boolean isStatCommand() {
        return true;
    }

    @Override
    public boolean isComputationCommand() {
        return false;
    }


    public String getStatKind() {
        return statKind;
    }
}
