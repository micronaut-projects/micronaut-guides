function updateStatus(n, started) {

    if (started) {
        onMove(n);
    }

    const game = GAMES[n];
    if (game.in_checkmate()) {
        onCheckmate(n);
    }
    else if (game.in_draw()) {
        onDraw(n);
    }
}

function onGameStart(n) {
    $.post('/game/start', { b: BLACK_NAMES[n], w: WHITE_NAMES[n]}, function (data) {
        const gameId = data;
        GAME_IDS[n] = gameId;
        $('#gameId' + n).text('Game ID: ' + gameId);
        window.setTimeout(function () {
            makeRandomMove(n);
        }, 2000); // delay a bit so the Game is persisted
    });
}

function onMove(n) {
    const game = GAMES[n];
    const history = game.history();
    const move = history[history.length - 1];

    $.post('/game/move/' + GAME_IDS[n], {
        player: other(n),
        fen: game.fen(),
        pgn: game.pgn(),
        move: move
    });
}

function onCheckmate(n) {
    const winner = other(n);
    $.post('/game/checkmate/' + GAME_IDS[n] + '/' + winner);
}

function onDraw(n) {
    $.post('/game/draw/' + GAME_IDS[n]);
}

function other(n) {
    return GAMES[n].turn() === 'b' ? 'w' : 'b';
}

function makeRandomMove(n) {

    const game = GAMES[n];

    if (game.game_over()) {
        restart(n);
        return;
    }

    const possibleMoves = game.moves();
    const moveIndex = Math.floor(Math.random() * possibleMoves.length);
    game.move(possibleMoves[moveIndex]);
    BOARDS[n].position(game.fen());
    updateStatus(n, true);

    window.setTimeout(function () {
        makeRandomMove(n);
    }, playDelay);
}

function restart(n) {
    BOARDS[n].position(FEN_INITIAL);
    GAMES[n].load(FEN_INITIAL);
    updateStatus(n, false);
    onGameStart(n);
}

function startGames() {
    $('#counts').toggle();

    playDelay = parseInt($('#playDelay').val(), 10);

    const rowCount = parseInt($('#rowCount').val(), 10);
    const gamesPerRow = parseInt($('#gamesPerRow').val(), 10);
    const hWidth = (window.innerWidth - 50) / gamesPerRow;
    const vWidth = window.innerHeight / rowCount - 50;
    const gameWidth = Math.min(400, hWidth, vWidth);

    for (let r = 0; r < rowCount; r++) {

        const gamesContainer = $('#gamesContainer');
        gamesContainer.append(
            '<div id="gamesRow' + r + '" class="gamesRow"></div>'
        );

        for (let c = 0; c < gamesPerRow; c++) {

            const n = r * gamesPerRow + c;

            const gamesRow = $('#gamesRow' + r);
            gamesRow.append(
                '<div class="gameContainer" style="width: ' + gameWidth + 'px">' +
                '<div id="chessboard' + n + '"></div>' +
                '<div><span id="gameId' + n + '"></span></div>' +
                '</div>'
            );

            GAMES[n] = new Chess();
            BLACK_NAMES[n] = 'b' + n;
            WHITE_NAMES[n] = 'w' + n;

            BOARDS[n] = Chessboard('chessboard' + n, {
                position: 'start',
                appearSpeed: 0,
                moveSpeed: 0
            });

            updateStatus(n, false);
            onGameStart(n);
        }
    }
}

const FEN_INITIAL = 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1';
const GAME_IDS = [];
const BOARDS = [];
const BLACK_NAMES = [];
const WHITE_NAMES = [];
const GAMES = [];
let playDelay = 5;

$('#startButton').on('click', startGames);
