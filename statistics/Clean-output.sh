#!/bin/sh
cat ./Output.log | grep '^STATISTICS\|^AGENT' > Output-Stats.log
