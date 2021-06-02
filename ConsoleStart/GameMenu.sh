#!/bin/zsh
BASEDIR=$(dirname "$0")
java --module-path $BASEDIR/JavaFX/lib --add-modules javafx.controls,javafx.fxml -jar  $BASEDIR/chess.jar "$@"
