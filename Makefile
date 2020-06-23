dev-client:
	cd client && yarn start

dev-nrepl:
	PORT=4000 clj -R:nREPL -m nrepl.cmdline -p 3434 -i

dev-tmux:
	tmux new-session -d -s clap 'make dev-nrepl'
	tmux send "(require 'clap.core) (clap.core/-main)" ENTER
	tmux split-window
	tmux send 'make dev-client' ENTER
	tmux split-window
	tmux send 'echo "time to roll 🚀"' ENTER
	tmux a

prod-build-jar:
	echo "-. build client"
	cd client && yarn build

	echo "-. move to resources"
	cd .. 
	rm -rf resources/public
	mv client/build resources/public

	echo "-. build uberjar"
	clojure -Spom && clojure -A:uberjar clap.jar -C -m clap.core

	echo "-. cleanup client"
	rm -rf resources/public

prod-deploy:
	make prod-build-jar
	gcloud app deploy
	rm clap.jar