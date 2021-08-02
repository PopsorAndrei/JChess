package com.chess.gui;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.piece.Piece;
import com.chess.engine.player.MoveTransition;
import com.chess.engine.player.ai.MiniMax;
import com.chess.engine.player.ai.MoveStrategy;
import com.chess.pgn.FenUtilities;
import com.google.common.collect.Lists;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class Table extends Observable {
    private final JFrame gameFrame;
    private GameHistoryPanel gameHistoryPanel;  //maybe here need to be final
    private final TakenPiecesPanel takenPiecesPanel;

    private final BoardPanel boardPanel;
    public Board chessBoard; //maybe here needs to pe private
    private final MoveLog moveLog;
    private final GameSetup gameSetup;

    private Move computerMove;


    public Color lightTileColor = Color.decode("#FFFACD");
    public Color darkTileColor = Color.decode("#593E1A"); //maybe need to be protected

    public Color BorderColor = Color.decode("#65350F");
    public String pieceIconPath = "art/fancy/";
    //pt click event
    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;


    private BoardDirection boardDirection;
    public boolean highlightLegalMoves = true;
    public boolean highlightIlegalMoves = true;

    //chesti pt mine
    public int frameNumber = 0;


    private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(700, 600);
    private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 350);
    private final static Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10);


    private static final Table INSTANCE = new Table();

    
    //table constructor might have to be private

    public Table() {
        this.gameFrame = new JFrame("JChess");
        this.gameFrame.setLayout(new BorderLayout());

        ImageIcon image = new ImageIcon("fin.png");

        this.gameFrame.setIconImage(image.getImage());

        final JMenuBar tableMenuBar = createTableMenuBar();
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.chessBoard = Board.createStandardBoard();
        this.gameHistoryPanel = new GameHistoryPanel();
        this.takenPiecesPanel = new TakenPiecesPanel();
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.boardPanel = new BoardPanel();
        this.moveLog = new MoveLog();
        this.addObserver(new TableGameAIWatcher());
        //cred ca aci mai trebe sa fac cv
        this.gameSetup = new GameSetup(this.gameFrame, true);
        this.boardDirection = BoardDirection.NORMAL;
        //

        //
        this.gameFrame.add(this.takenPiecesPanel, BorderLayout.WEST);
        this.gameFrame.add(this.gameHistoryPanel, BorderLayout.EAST);
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.setVisible(true);
        this.gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.gameFrame.setResizable(false);


        //form now on estetics
        //making the borderd
        //  this.gameFrame.getRootPane().setBorder(BorderFactory.createMatteBorder(4,4,4,4,Color.BLUE));
    }

    public static Table get() {
        return INSTANCE;
    }

    public void show() {
        Table.get().getMoveLog().clear();
        Table.get().getGameHistoryPanel().redo(chessBoard, Table.get().getMoveLog());
        Table.get().getTakenPiecesPanel().redo(Table.get().getMoveLog());
        Table.get().getBoardPanel().drawBoard(Table.get().getGameBoard());

    }

    public GameSetup getGameSetup() {
        return this.gameSetup;
    }

    private Board getGameBoard() {
        return this.chessBoard;
    }

    public JFrame getGameFrame() {
        return this.gameFrame;
    }

    private JMenuBar createTableMenuBar() {
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        tableMenuBar.add(createPreferencesMenu());
        tableMenuBar.add(changeBackground());
        tableMenuBar.add(createOptionsMenu());
        tableMenuBar.add(createNewGameMenu());
      //  this.getGameFrame().repaint();

        return tableMenuBar;
    }

    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");


        final JMenuItem getFEN = new JMenuItem("Get FEN");

        getFEN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               new FenTextBox(FenUtilities.createFENFromGame(getGameBoard()));
            }
        });
        fileMenu.add(getFEN);

        final JMenuItem openPGN = new JMenuItem("Load PGN file");

        openPGN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoadFen();
            }
        });
        fileMenu.add(openPGN);
        final JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);


        return fileMenu;
    }


    private class BoardPanel extends JPanel {
        final List<TilePanel> boardTiles;


        BoardPanel() {
            super(new GridLayout(8, 8));

            this.setBorder(BorderFactory.createMatteBorder(10, 5, 10, 5, BorderColor));

            this.boardTiles = new ArrayList<>();
            for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
                final TilePanel tilePanel = new TilePanel(this, i);//def some parameters here
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }

        public void drawBoard(final Board board) {
            removeAll();

            System.out.println(frameNumber++);
            for (final TilePanel tilePanel : boardDirection.traverse(boardTiles)) {

                tilePanel.drawTile(board);
                add(tilePanel);

            }
            this.
                    validate();
            repaint();
        }
    }

    public static class MoveLog {
        private final List<Move> moves;

        MoveLog() {

            moves = new ArrayList<>();
        }

        public List<Move> getMoves() {
            return moves;
        }

        public void addMove(final Move move) {
            moves.add(move);
        }

        public int size() {
            return moves.size();
        }

        public void clear() {
            this.moves.clear();
        }

        public Move removeMove(int index) {
            return this.moves.remove(index);
        }

        public boolean removeMove(final Move move) {
            return this.moves.remove(move);
        }

    }


    enum PlayerType {
        HUMAN,
        COMPUTER
    }


    private class TilePanel extends JPanel {

        private final int tileId;

        TilePanel(final BoardPanel boardPanel,
                  final int tileId) {
            super(new GridBagLayout());
            this.tileId = tileId;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColour();
            assignTilePieceIcon(chessBoard);
            //   highlightLegalMoves(chessBoard);
            //  highlightIllegalSelfCheckMove(chessBoard);


            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(final MouseEvent e) {
                    if (isRightMouseButton(e)) {
                        //in caz ca am o piesa selectat cu click dreapta deselectez
                        sourceTile = null;
                        destinationTile = null;
                        humanMovedPiece = null;
                        boardPanel.drawBoard(chessBoard);

                        //testing porpous
                        System.out.println(chessBoard.getTile(tileId).getPiece().isFirstMove());


                    } else if (isLeftMouseButton(e)) {
                        //until if(sourcetile== null) doing dumb shit


                        //selecting a piece
                        if (sourceTile == null || chosingAnotherPiece()) {      //  if (sourceTile ==null)
                            //first click
                            sourceTile = chessBoard.getTile(tileId);
                            humanMovedPiece = sourceTile.getPiece();

                            if (humanMovedPiece != null) {
                                if (humanMovedPiece.getPieceAliance() != chessBoard.currentPlayer().getAliance()) {
                                    humanMovedPiece = null;
                                    sourceTile = null;

                                }
                            }

                            if (humanMovedPiece == null) {
                                sourceTile = null;
                            }
                        } else {//aici se executa mutarea in sine
                            //second click
                            //moving selected piece;
                            destinationTile = chessBoard.getTile(tileId);
                            final Move move = Move.MoveFactory.CreateMove(chessBoard, sourceTile.getTileCoordinate(), destinationTile.getTileCoordinate());
                            final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);

                            if (transition.getMoveStatus().isDone()) {

                                chessBoard = transition.getTransitionBoard();
                                moveLog.addMove(move);

                            }
                            sourceTile = null;
                            destinationTile = null;
                            humanMovedPiece = null;


                        }
                        SwingUtilities.invokeLater(new Runnable() {//after every clcik do this
                            @Override
                            public void run() {
                                gameHistoryPanel.redo(chessBoard, moveLog);
                                takenPiecesPanel.redo(moveLog);
                                if (gameSetup.isAIPlayer(chessBoard.currentPlayer())) {
                                    Table.get().moveMadeUpdate(PlayerType.HUMAN);
                                }
                                boardPanel.drawBoard(chessBoard);

                            }
                        });
                    }
                    if (getGameBoard().currentPlayer().isInCheckMate()) {
                        String winningMessage;
                        if (getGameBoard().currentPlayer().getAliance().toString().equals("B")) {
                            winningMessage = "White Player won!";
                        } else {
                            winningMessage = "Black Player won!";
                        }
                        new CheckMateMessage(winningMessage);
                    }
                    if (getGameBoard().currentPlayer().isInStaleMate()) {
                        new CheckMateMessage("Stalemate, no one wins");
                    }
                }


                public boolean chosingAnotherPiece() {
                    if (chessBoard.getTile(tileId).isTileOccupied()) {
                        if (chessBoard.getTile(tileId).getPiece().getPieceAliance() == chessBoard.currentPlayer().getAliance()) {
                            return true;
                        }
                    }
                    return false;
                }

                @Override
                public void mousePressed(final MouseEvent e) {

                }

                @Override
                public void mouseReleased(final MouseEvent e) {

                }

                @Override
                public void mouseEntered(final MouseEvent e) {

                }

                @Override
                public void mouseExited(final MouseEvent e) {

                }
            });
            validate();


        }

        public void drawTile(final Board board) {
            assignTileColour();
            assignTilePieceIcon(board);
            highlightLegalMoves(board);
            highlightIllegalSelfCheckMove(board);
            validate();
            repaint();
        }

        private void assignTilePieceIcon(final Board board) {
            this.removeAll();
            if (board.getPiece(this.tileId) != null) {
                try {

                    final BufferedImage image = ImageIO.read(new File(pieceIconPath +
                            board.getPiece(this.tileId).getPieceAliance().toString() + "" +
                            board.getPiece(this.tileId).toString() +
                            ".png"));
                        //resire poza

                    //another attempt

                    BufferedImage newImage = resize(image,50,50);

                    Image dimg = image.getScaledInstance(50, 50,
                            Image.SCALE_SMOOTH);

                    ImageIcon imageIcon = new ImageIcon(dimg);
                    //another attempt
                    ImageIcon displayedImage = new ImageIcon((image));

                    Image auxImage = displayedImage.getImage();
                    Image modifiedAuxImage = auxImage.getScaledInstance(50,50,Image.SCALE_SMOOTH);

                    displayedImage = new ImageIcon(modifiedAuxImage);


                    //this might be wrong
                    /*
                    final BufferedImage img=new ImgUtils().scaleImage(50,50,pieceIconPath +
                            board.getPiece(this.tileId).getPieceAliance().toString() + "" +
                            board.getPiece(this.tileId).toString() +
                            ".png");
                    //this is commented because i use another way and o dont know if its right
                     */


                    JLabel label = new JLabel(new ImageIcon(dimg));


                    JLabel label2 = new JLabel(imageIcon);





                    //mai sus incercari resise poza
                    JLabel testLabel = new JLabel(displayedImage);//new ImageIcon((image))
                    testLabel.setVerticalAlignment(JLabel.CENTER);
                    testLabel.setVerticalAlignment(JLabel.CENTER);
                    add(label);

                    //add(new JLabel(new ImageIcon(image)));
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
        public  BufferedImage resize(BufferedImage img, int newW, int newH) {
            Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
            BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.SCALE_SMOOTH);

            Graphics2D g2d = dimg.createGraphics();
            g2d.drawImage(tmp, 0, 0, null);
            g2d.dispose();

            return dimg;
        }

        private void assignTileColour() {
            if (BoardUtils.EIGHT_RANK[this.tileId] ||
                    BoardUtils.SIXTH_RANK[this.tileId] ||
                    BoardUtils.FOURTH_RANK[this.tileId] ||
                    BoardUtils.SECOND_RANK[this.tileId]) {
                setBackground(this.tileId % 2 == 0 ? lightTileColor : darkTileColor);
            } else if (BoardUtils.SEVENTH_RANK[this.tileId] ||
                    BoardUtils.FIFTH_RANK[this.tileId] ||
                    BoardUtils.THIRD_RANK[this.tileId] ||
                    BoardUtils.FIRST_RANK[this.tileId]) {
                setBackground(this.tileId % 2 == 0 ? darkTileColor : lightTileColor);
            }
        }

        private void highlightLegalMoves(final Board board) {

            Collection<Move> legalMovesWithTheSamePiece = new ArrayList<>();
            for (Move move : board.currentPlayer().getLegalMoves()) {
                if (move.getMovedPiece().equals(humanMovedPiece)) {
                    legalMovesWithTheSamePiece.add(move);
                }
            }


            if (highlightLegalMoves) {    //humanPieceLegalMoves(board)
                for (final Move move : legalMovesWithTheSamePiece) {
                    MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
                    if (transition.getMoveStatus().isDone()) {
                        if (move.getDestinationCoordonate() == this.tileId) {
                            try {
                                JLabel testLabel = new JLabel();
                                ImageIcon test = new ImageIcon(ImageIO.read(new File("art/misc/green_dot.png")));
                                testLabel.setIcon(test);
                                testLabel.setVerticalAlignment(JLabel.TOP);
                                testLabel.setIconTextGap(-30);
                                testLabel.setAlignmentX(0);
                                testLabel.setAlignmentY(0);
                              //  testLabel.setBorder(BorderFactory.createLineBorder(Color.GREEN));
                              //  setBorder(BorderFactory.createLineBorder(Color.GREEN));
                                //   add(new JLabel(new ImageIcon(ImageIO.read(new File("art/misc/green_dot.png")))));
                                add(testLabel);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
                // de aici bag prostii pr rocada


            }

        }

        private void highlightIllegalSelfCheckMove(final Board board) {
            if (highlightIlegalMoves) {      //humanPieceLegalMoves(board)

                for (final Move move : humanPieceLegalMoves(board)) {
                    MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
                    if (transition.getMoveStatus().leavesPlayerInCheck()) {
                        if (move.getDestinationCoordonate() == this.tileId) {
                            try {
                                add(new JLabel(new ImageIcon(ImageIO.read(new File("art/misc/red_dot.png")))));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

        }

        private Collection<Move> humanPieceLegalMoves(final Board board) {
            if (humanMovedPiece != null && humanMovedPiece.getPieceAliance() == board.currentPlayer().getAliance()) {


                return humanMovedPiece.calculateLegalMoves(board);
            }
            return Collections.emptyList();
        }
    }

    private JMenu createPreferencesMenu() {
        final JMenu preferencesMenu = new JMenu("Preferences");
        final JMenuItem flipBoardMenuItem = new JMenuItem("Flip Board");

        flipBoardMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boardDirection = boardDirection.opposite();
                boardPanel.drawBoard(chessBoard);
            }
        });
        preferencesMenu.add(flipBoardMenuItem);
        final JMenuItem changeStyleMenuItem = new JMenuItem("change style");
        changeStyleMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pieceIconPath != "art/fancy2/") {
                    pieceIconPath = "art/fancy2/";
                } else {
                    pieceIconPath = "art/fancy/";
                }
                boardPanel.drawBoard(chessBoard);
            }
        });
        preferencesMenu.addSeparator();
        preferencesMenu.add(changeStyleMenuItem);
        preferencesMenu.addSeparator();
        //adding the move highlither obtion
        final JCheckBoxMenuItem legalMovesHighlighterCheckBox = new JCheckBoxMenuItem("highlight LegalMoves", true);
        legalMovesHighlighterCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                highlightLegalMoves = legalMovesHighlighterCheckBox.isSelected();
            }
        });
        preferencesMenu.add(legalMovesHighlighterCheckBox);
        //adding the IllegalMovesIncCHek obtion
        final JCheckBoxMenuItem illegalMovesCasueCheck = new JCheckBoxMenuItem("pottential but Illegal moves", true);
        illegalMovesCasueCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                highlightIlegalMoves = illegalMovesCasueCheck.isSelected();
            }
        });
        preferencesMenu.addSeparator();
        preferencesMenu.add(illegalMovesCasueCheck);

        return preferencesMenu;
    }

    private JMenu createOptionsMenu() {
        final JMenu optionsMenu = new JMenu("Obtions");

        final JMenuItem setupGameMenuItem = new JMenuItem("SetupGame");
        setupGameMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Table.get().getGameSetup().promptUser();
                Table.get().setupUpdate(Table.get().getGameSetup());
            }
        });
        optionsMenu.add(setupGameMenuItem);
        return optionsMenu;
    }

    //newGameButton
    private JMenu createNewGameMenu() {
        final JMenu newGame = new JMenu("Game");

        final JMenuItem startNewGame = new JMenuItem("start new game");
        startNewGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              //  setNewGameBoard();
                chessBoard =  Board.createStandardBoard();
                getMoveLog().clear();
                //   getGameFrame().dispose();
                  // new Table();
            }
        });

        final JMenuItem undoLastMove = new JMenuItem("undo last Move");
        undoLastMove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Move> a = getMoveLog().getMoves();
                System.out.println(a.size());
                if(a.size()>1) {
                    chessBoard = a.get(a.size() - 1).getBoard();
                    getMoveLog().getMoves().remove(a.size()-1);
                    boardPanel.drawBoard(chessBoard);

                }
                else {
                    System.out.println("Sper");
                    chessBoard = Board.createStandardBoard();
                    boardPanel.drawBoard(chessBoard);
                }
            }
        });
        final JMenuItem undoLast2Move = new JMenuItem("undo last 2 Moves");
        undoLast2Move.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Move> a = getMoveLog().getMoves();
                System.out.println(a.size());
                if(a.size()>2) {
                    chessBoard = a.get(a.size() - 2).getBoard();
                    getMoveLog().getMoves().remove(a.size()-1);
                    getMoveLog().getMoves().remove(a.size()-1);
                    boardPanel.drawBoard(chessBoard);

                }


            }
        });


        newGame.add(startNewGame);
        newGame.add(undoLastMove);
        newGame.add(undoLast2Move);
        return newGame;
    }

    private void setupUpdate(final GameSetup gameSetup) {
        setChanged();
        notifyObservers(gameSetup);
    }

    private static class TableGameAIWatcher
            implements Observer {
        @Override
        public void update(final Observable o, final Object arg) {

            if (Table.get().getGameSetup().isAIPlayer(Table.get().getGameBoard().currentPlayer())
                    &&
                    !Table.get().getGameBoard().currentPlayer().isInCheckMate() &&
                    !Table.get().getGameBoard().currentPlayer().isInStaleMate()) {
                //create AI thread
                //execute ai work

                final AIThinkTank thinkTank = new AIThinkTank();
                thinkTank.execute();
            }
            if (Table.get().getGameBoard().currentPlayer().isInCheckMate()) {
                System.out.println("Game over " + Table.get().getGameBoard().currentPlayer() + "is in checkmate");
            }
            if (Table.get().getGameBoard().currentPlayer().isInStaleMate()) {
                System.out.println("Game over " + Table.get().getGameBoard().currentPlayer() + "is in stalemate");
            }
        }
    }

    public void updateGameBoard(final Board board) {
        this.chessBoard = board;
    }

    public void updateComputerMove(final Move move) {
        this.computerMove = move;
    }

    private MoveLog getMoveLog() {
        return this.moveLog;
    }

    private GameHistoryPanel getGameHistoryPanel() {
        return this.gameHistoryPanel;
    }

    private TakenPiecesPanel getTakenPiecesPanel() {
        return this.takenPiecesPanel;
    }

    private BoardPanel getBoardPanel() {
        return this.boardPanel;
    }

    private void moveMadeUpdate(final PlayerType playerType) {
        setChanged();
        notifyObservers(playerType);

    }

    private static class AIThinkTank extends SwingWorker<Move, String> {

        private AIThinkTank() {

        }

        @Override
        public Move doInBackground() throws Exception {

            final MoveStrategy miniMax = new MiniMax(Table.get().getGameSetup().getSearchDepth());
            //4 poate la minimax trebuiau alti parametri
            //Table.get().getGameSetup().getSearchDepth();


            final Move bestMove = miniMax.execute(Table.get().getGameBoard());

            return bestMove;
        }

        @Override
        public void done() {
            try {
                final Move bestMove = get();
                Table.get().updateComputerMove(bestMove);
                Table.get().updateGameBoard(Table.get().getGameBoard().currentPlayer().makeMove(bestMove).getTransitionBoard());
                Table.get().getMoveLog().addMove(bestMove);
                Table.get().getGameHistoryPanel().redo(Table.get().getGameBoard(), Table.get().getMoveLog());
                Table.get().getTakenPiecesPanel().redo(Table.get().getMoveLog());
                Table.get().getBoardPanel().drawBoard(Table.get().getGameBoard());
                Table.get().moveMadeUpdate(PlayerType.COMPUTER);
            //    String a = a.toc

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }


    public enum BoardDirection {
        NORMAL {
            @Override
            List<TilePanel> traverse(final List<TilePanel> boardTiles) {
                return boardTiles;
            }

            @Override
            BoardDirection opposite() {
                return BoardDirection.FLIPPED;
            }
        },
        FLIPPED {
            @Override
            List<TilePanel> traverse(final List<TilePanel> boardTiles) {
                return Lists.reverse(boardTiles);
            }

            @Override
            BoardDirection opposite() {
                return BoardDirection.NORMAL;
            }
        };


        abstract List<TilePanel> traverse(List<TilePanel> boardTiles);

        abstract BoardDirection opposite();
    }

//Modificari facute pe cont proriu

    private JMenu changeBackground() {

        final JMenu changeBackgroundMenu = new JMenu("Background");


        final JMenuItem setDarkModeBackground = new JMenuItem("Style 1");
        setDarkModeBackground.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lightTileColor = Color.decode("#FFFACD");
                darkTileColor = Color.decode("#593E1A");
                boardPanel.drawBoard(chessBoard);
            }
        });

        changeBackgroundMenu.add(setDarkModeBackground);


        final JMenuItem setMode2Background = new JMenuItem("Style 2");
        setMode2Background.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                lightTileColor = Color.decode("#FFFFCC");
                darkTileColor = Color.decode("#808080");
                boardPanel.drawBoard(chessBoard);
            }
        });
        changeBackgroundMenu.add(setMode2Background);
        final JMenuItem setMode3Background = new JMenuItem("Style 3");
        setMode3Background.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lightTileColor = Color.decode("#eeeed1");
                darkTileColor = Color.decode("#799654");
                boardPanel.drawBoard(chessBoard);
            }
        });
        changeBackgroundMenu.add(setMode3Background);

        final JMenuItem setMode4Background = new JMenuItem("Style 4");
        setMode4Background.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lightTileColor = Color.decode("#f9e4b1");
                darkTileColor = Color.decode("#c98b0f");
                boardPanel.drawBoard(chessBoard);
            }
        });
        changeBackgroundMenu.add(setMode4Background);

        final JMenuItem setMode5Background = new JMenuItem("Style 5");
        setMode5Background.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lightTileColor = Color.decode("#e9c9a1");
                darkTileColor = Color.decode("#cea369");
                boardPanel.drawBoard(chessBoard);
            }
        });
        changeBackgroundMenu.add(setMode5Background);

        final JMenuItem setMode6Background = new JMenuItem("Style 6");
        setMode6Background.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lightTileColor = Color.decode("#e9d2ba");
                darkTileColor = Color.decode("#9d6b5c");
                boardPanel.drawBoard(chessBoard);
            }
        });
        changeBackgroundMenu.add(setMode6Background);


        return changeBackgroundMenu;
    }


    //INCERCARILE MELE DISPERATE///////
    /////////////////////////

    public Color getLightTileColor() {
        return this.lightTileColor;
    }

    public class CheckMateMessage extends JFrame implements ActionListener {

        JButton buttonNewGame;
        JButton buttonClose;

        public CheckMateMessage(String text) {
            this.setTitle("Chess Menu");
            this.setSize(360, 150);
            this.setVisible(true);
            this.getContentPane().setBackground(Color.WHITE);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


            JLabel label = new JLabel(text, JLabel.CENTER);

            label.setAlignmentX(0);
            label.setAlignmentY(100);
            //  label.setBorder(border);
            //   label.setPreferredSize(new Dimension(30, 15));


            //newGameButton
            buttonNewGame = new JButton();
            buttonNewGame.setBounds(70, 70, 70, 30);
            buttonNewGame.setText("New Game");
            buttonNewGame.setFont(new Font("MV Boli", Font.PLAIN, 10));
            buttonNewGame.setFocusable(false);
            buttonNewGame.addActionListener(this);
            this.add(buttonNewGame);


            //closeGameButton
            buttonClose = new JButton();
            buttonClose.setBounds(210, 70, 70, 30);
            buttonClose.setText("CLose");
            buttonClose.setFont(new Font("MV Boli", Font.PLAIN, 10));
            buttonClose.setFocusable(false);
            buttonClose.addActionListener(this);
            this.add(buttonClose);
            this.add(label);


        }

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == buttonNewGame) {
                getGameFrame().dispose();
                dispose();
                Table a = new Table();
                a.show();
                repaint();

                //  a.show();
                // repaint();
                // boardPanel.drawBoard(chessBoard);
                gameHistoryPanel.redo(chessBoard, moveLog);
                takenPiecesPanel.redo(moveLog);
                if (gameSetup.isAIPlayer(chessBoard.currentPlayer())) {
                    Table.get().moveMadeUpdate(PlayerType.HUMAN);
                }
                boardPanel.drawBoard(chessBoard);

            }
            if (e.getSource() == buttonClose) {
                System.exit(0);
            }
        }
    }

    public void setGameHistoryPanel() {
        this.gameHistoryPanel = new GameHistoryPanel();
    }

    public void setNewGameBoard() {
        this.getGameFrame().dispose();
        new Table(getLightTileColor(), getDarkTileColor());
    }

    public Color getLightTileColour() {
        return this.lightTileColor;
    }

    public Color getDarkTileColor() {
        return this.darkTileColor;
    }

    public Table(Color lightTileColor, Color darkTileColor) {
        this.lightTileColor = lightTileColor;
        this.darkTileColor = darkTileColor;
        this.gameFrame = new JFrame("JChess");
        this.gameFrame.setLayout(new BorderLayout());

        ImageIcon image = new ImageIcon("fin.png");

        this.gameFrame.setIconImage(image.getImage());

        final JMenuBar tableMenuBar = createTableMenuBar();
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.chessBoard = Board.createStandardBoard();
        this.gameHistoryPanel = new GameHistoryPanel();
        this.takenPiecesPanel = new TakenPiecesPanel();
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.boardPanel = new BoardPanel();
        this.moveLog = new MoveLog();
        this.addObserver(new TableGameAIWatcher());
        //cred ca aci mai trebe sa fac cv
        this.gameSetup = new GameSetup(this.gameFrame, true);
        this.boardDirection = BoardDirection.NORMAL;
        //

        //
        this.gameFrame.add(this.takenPiecesPanel, BorderLayout.WEST);
        this.gameFrame.add(this.gameHistoryPanel, BorderLayout.EAST);
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.setVisible(true);
        this.gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.gameFrame.setResizable(false);


        //form now on estetics
        //making the borderd
        //  this.gameFrame.getRootPane().setBorder(BorderFactory.createMatteBorder(4,4,4,4,Color.BLUE));
       /*
        gameHistoryPanel.redo(chessBoard,moveLog);
        takenPiecesPanel.redo(moveLog);
        if(gameSetup.isAIPlayer(chessBoard.currentPlayer())){
            Table.get().moveMadeUpdate(PlayerType.HUMAN);
        }
        boardPanel.drawBoard(chessBoard);
    }

        */
    }

    //CHESTI CU CLARITATEA IMAGINII
    public class ImgUtils {

        public BufferedImage scaleImage(int WIDTH, int HEIGHT, String filename) {
            BufferedImage bi = null;
            try {
                ImageIcon ii = new ImageIcon(filename);//path to image
                bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.SCALE_SMOOTH);
                Graphics2D g2d = (Graphics2D) bi.createGraphics();
                g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY));
                g2d.drawImage(ii.getImage(), 0, 0, WIDTH, HEIGHT, null);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return bi;
        }

    }
    //aici ii sa faca board din String
    public class LoadFen extends JFrame  {

        LoadFen(){
            this.setTitle("FEN text ");
            this.setSize(400,125);
            this.setVisible(true);
            this.getContentPane().setBackground(Color.WHITE);
            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            this.setResizable(true); //this need to be false, true olny for testing
            this.setLayout(null);

            JLabel label = new JLabel("Enter FEN text");
            label.setBounds(10,20,80,25);
            this.add(label);


            //here we are adding the text
            JTextField FENtext = new JTextField();
            FENtext.setBounds(100,20,165,25);
            this.add(FENtext);

            //adding the button
            JButton button = new JButton("Generate");
            button.setBounds(140,50,100,25);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    FENtext.getText();
                   // System.out.println(FENtext.getText());
                    System.out.println(FenUtilities.createBoardFromFENString(FENtext.getText()));
                    chessBoard = FenUtilities.createBoardFromFENString(FENtext.getText());
                    getMoveLog().clear();

                    dispose();


                }
            });

            this.add(button);



        }
    }
}



