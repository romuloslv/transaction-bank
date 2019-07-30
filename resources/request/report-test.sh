#!/bin/sh

TEST_PAGE="$(which google-chrome) --no-sandbox $PWD/target/coverage/index.html"

lein cloverage --runner :midje 2> /dev/null
$TEST_PAGE 2> /dev/null
