#!/usr/bin/env bash
if [ "$TRAVIS_BRANCH" = 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
    openssl aes-256-cbc -K $encrypted_5d4a1b060ac6_key -iv $encrypted_5d4a1b060ac6_iv -in cd/codesign.asc.enc -out cd/codesign.asc -d
    gpg --fast-import cd/codesign.asc
fi
