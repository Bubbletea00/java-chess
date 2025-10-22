package util;

public class FenTest {

    static void main() {
        new Fen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
//        new Fen(null);
//        new Fen("rnbqkbnr/pppppppp8/8/1PPP2P1/8/PP1PPPPPP2PPPPPPPPPP1/QQ w KQkq - 0 1");

        FenUtils fenUtils = new FenUtils();

        fenUtils.test();

    }
}
