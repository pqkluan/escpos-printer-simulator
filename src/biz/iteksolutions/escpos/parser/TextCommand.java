package biz.iteksolutions.escpos.parser;

public class TextCommand extends Command implements IContentOutput {

    private boolean isChinese = false;

    private StringBuilder sb;

    public TextCommand() {
        sb = new StringBuilder();
    }

    @Override
    public boolean addChar(Character c) {
        if (Command.commandsMap.get(c) != null) {
            return false;
        }
        sb.append(c);
        return true;
    }

    public void setChinese(boolean chinese) {
        isChinese = chinese;
    }
    
    public boolean getIsChinese() {
		return isChinese;
	}

	@Override
    public String getText() {
        String result = sb.toString();
        return result;
    }
}
