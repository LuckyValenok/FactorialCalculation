package net.luckyvalenok.factorialcalculation;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.TerminalEmulatorAutoCloseTrigger;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;

public class Main {
    
    public static void main(String[] args) throws InterruptedException, IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Введите число, которые будет использовать при счете факториала.");
        BigDecimal result = calculateFactorial(readBigDecimal(reader));
        System.out.println("Введите время (мс), раз в которое будет сменяться цвет фона.");
        BigDecimal time = readBigDecimal(reader);
        
        DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
        Terminal terminal;
        try {
            defaultTerminalFactory.setInitialTerminalSize(new TerminalSize(result.toString().length() + 10, 10));
            defaultTerminalFactory.setTerminalEmulatorTitle("FactorialCalculation");
            defaultTerminalFactory.setTerminalEmulatorFrameAutoCloseTrigger(TerminalEmulatorAutoCloseTrigger.CloseOnExitPrivateMode);
            terminal = defaultTerminalFactory.createTerminal();
            
            terminal.setCursorVisible(false);
            int columns = terminal.getTerminalSize().getColumns();
            int rows = terminal.getTerminalSize().getRows();
            String line = getLine((result + "").length());
    
            TerminalPosition startPosition = terminal.getCursorPosition().withRelativeColumn(columns / 2 - line.length() / 2 - 1);
            
            Color color = Color.LIGHT;
            while (true) {
                color.changeColor(terminal);
                color = Color.getRevert(color);
                
                printLine(terminal, "╔" + line + "╗", startPosition, rows / 2 - 2);
                printLine(terminal, "|" + result + "|", startPosition, rows / 2 - 1);
                printLine(terminal, "╚" + line + "╝", startPosition, rows / 2);
                
                Thread.sleep(time.longValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static String getLine(int i) {
        return StringUtils.repeat('═', i);
    }
    
    private static void printLine(Terminal terminal, String line, TerminalPosition cursor, int row) throws IOException {
        terminal.setCursorPosition(cursor.withRelativeRow(row));
        for (char c : line.toCharArray()) {
            terminal.putCharacter(c);
        }
        terminal.flush();
    }
    
    private static BigDecimal calculateFactorial(BigDecimal i) {
        if (i.compareTo(BigDecimal.ONE) < 0)
            return BigDecimal.ONE;
        else
            return i.multiply(calculateFactorial(i.add(BigDecimal.ONE.negate())));
    }
    
    private static BigDecimal readBigDecimal(BufferedReader reader) throws IOException {
        try {
            return new BigDecimal(reader.readLine());
        } catch (NumberFormatException e) {
            System.out.println("Вы ввели не число. Попробуйте снова.");
            return readBigDecimal(reader);
        }
    }
    
    private enum Color {
        LIGHT(TextColor.ANSI.WHITE, TextColor.ANSI.BLACK),
        DARK(TextColor.ANSI.BLACK, TextColor.ANSI.WHITE);
        
        TextColor background;
        TextColor foreground;
        
        Color(TextColor background, TextColor foreground) {
            this.background = background;
            this.foreground = foreground;
        }
        
        private static Color getRevert(Color color) {
            if (color == LIGHT)
                return DARK;
            else
                return LIGHT;
        }
        
        private void changeColor(Terminal terminal) throws IOException {
            terminal.setBackgroundColor(background);
            terminal.setForegroundColor(foreground);
        }
    }
}