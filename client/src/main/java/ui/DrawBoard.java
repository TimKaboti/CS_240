package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static ui.EscapeSequences.*;

public class DrawBoard {

    private static final int BOARD_HEIGHT = 8;
    private static final int BOARD_WIDTH = 8;
    private static final int SQUARE_WIDTH = 3;
    private static final String EMPTY = "   ";
    private static final String K = " K ";
    private static final String Q = " Q ";
    private static final String B = " B ";
    private static final String N = " N ";
    private static final String R = " R ";
    private static final String P = " P ";

    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawHeader(out);
        drawGrid(out);
        drawHeader(out);
        drawDivider(out);
        drawReverseHeader(out);
        drawGrid(out);
        drawReverseHeader(out);
//        drawTicTacToeBoard(out);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void drawHeader(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
        out.print("    h  g  f  e  d  c  b  a    ");
        out.print(SET_BG_COLOR_WHITE);
        out.print("\n");
    }

    private static void drawReverseHeader(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
        out.print("    a  b  c  d  e  f  g  h    ");
        out.print(SET_BG_COLOR_WHITE);
        out.print("\n");
    }

    private static void drawDivider(PrintStream out){
        out.print(SET_BG_COLOR_BLACK);
        for(int i = 0; i < 10; i++){
            out.print(EMPTY);
        }
        out.print(SET_BG_COLOR_WHITE);
        out.print("\n");
    }

    private static void drawGrid(PrintStream out){
        for(int i = 1; i < 9; i++){
            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.print(" " + i + " ");

            out.print("\n");

        }
        out.print(EMPTY);
        out.print(SET_BG_COLOR_WHITE);
        out.print("\n");

    }
}
