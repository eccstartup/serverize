serverize
=========

Turn arbitrary shell program into a web server

1. combine inputbox and console (try https://github.com/jcubic/jquery.terminal)
2. stderr

Usage: `java Serverizer [port] [program]`

Configuration via environment variables:
- MAX_SESSIONS, default: 5
- SESSION_WAIT_SECONDS, default: 10
- READER_THREAD_BUFFER_SIZE, default: 1024
- SLEEP_INTERVAL_MS, default: 10 000
- SESSION_TIMEOUT_MS, default: 10 000

