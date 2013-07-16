serverize
=========

This tool turns an arbitrary shell program (especially REPLs) into a web server.
Users can create new instances of the shell program and interact with them using a web interface.
By using this interface, users can have access to the shell program without actually installing or running it on their own machines.
This could be especially helpful for teaching programming languages (those with a REPL, of course) in a class.

Note that the program instances created by this tool have the same privilege as the user who runs this tool.
Some programs such as irb (interactive ruby) have access to the network and the underlying filesystem.
It is your responsibility to make sure that the exposed web interface is not a security threat to your system.
For example, you are supposed to run this tool under a jailed user account with limited access to the system settings.

Usage: `java Serverizer [port] [program]`

Configuration via environment variables:
- `MAX_SESSIONS`, default: 5
- `SESSION_WAIT_SECONDS`, default: 10
- `READER_THREAD_BUFFER_SIZE`, default: 1024 (bytes)
- `SLEEP_INTERVAL_MS`, default: 10 000
- `SESSION_TIMEOUT_MS`, default: 10 000

TODO items:
1. combine inputbox and console (try https://github.com/jcubic/jquery.terminal)
2. stderr
