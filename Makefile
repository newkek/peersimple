VER=1.0.4
CONFDIR=config

BASIC=$(CONFDIR)/basic.cfg
DOMINO=$(CONFDIR)/domino.cfg
BIGGRAPH=$(CONFDIR)/big_graph.cfg
BIGLATENCY=$(CONFDIR)/big_latency.cfg
MULTIFAULTS=$(CONFDIR)/multi_faults.cfg


.PHONY: all run clean doc release

all:
	javac -classpath src:jep-2.3.0.jar:djep-1.0.0.jar `find src -name "*.java"`

run: 
	java -classpath src:jep-2.3.0.jar:djep-1.0.0.jar peersim/Simulator $(BASIC)

domino: 
	java -classpath src:jep-2.3.0.jar:djep-1.0.0.jar peersim/Simulator $(DOMINO)

big_graph: 
	java -classpath src:jep-2.3.0.jar:djep-1.0.0.jar peersim/Simulator $(BIGGRAPH)

big_latency: 
	java -classpath src:jep-2.3.0.jar:djep-1.0.0.jar peersim/Simulator $(BIGLATENCY)

multi_faults: 
	java -classpath src:jep-2.3.0.jar:djep-1.0.0.jar peersim/Simulator $(MULTIFAULTS)


clean:
	rm -f `find . -name "*.class"`
doc:
	rm -rf doc/*
	javadoc -overview overview.html -classpath src:jep-2.3.0.jar:djep-1.0.0.jar -d doc \
                -group "Peersim" "peersim*" \
                -group "Examples" "example.*" \
		peersim \
		peersim.cdsim \
		peersim.config \
		peersim.core \
		peersim.dynamics \
		peersim.edsim \
		peersim.graph \
		peersim.rangesim \
		peersim.reports \
		peersim.transport \
		peersim.util \
		peersim.vector \
		example.aggregation \
		example.loadbalance \
		example.edaggregation \
		example.hot \
		example.newscast 

docnew:
	rm -rf doc/*
	javadoc -overview overview.html -docletpath peersim-doclet.jar -doclet peersim.tools.doclets.standard.Standard -classpath src:jep-2.3.0.jar:djep-1.0.0.jar -d doc \
                -group "Peersim" "peersim*" \
                -group "Examples" "example.*" \
		peersim \
		peersim.cdsim \
		peersim.config \
		peersim.core \
		peersim.dynamics \
		peersim.edsim \
		peersim.graph \
		peersim.rangesim \
		peersim.reports \
		peersim.transport \
		peersim.util \
		peersim.vector \
		example.aggregation \
		example.loadbalance \
		example.hot \
		example.edaggregation \
		example.newscast 


release: clean all docnew
	rm -fr peersim-$(VER)
	mkdir peersim-$(VER)
	cp -r doc peersim-$(VER)
	cp Makefile overview.html README CHANGELOG RELEASE-NOTES build.xml peersim-doclet.jar peersim-$(VER)
	mkdir peersim-$(VER)/example
	cp example/*.txt peersim-$(VER)/example
	mkdir peersim-$(VER)/src
	cp --parents `find src/peersim src/example -name "*.java"` peersim-$(VER)
	cd src ; jar cf ../peersim-$(VER).jar `find peersim example -name "*.class"`
	mv peersim-$(VER).jar peersim-$(VER)
	cp jep-2.3.0.jar peersim-$(VER)
	cp djep-1.0.0.jar peersim-$(VER)
