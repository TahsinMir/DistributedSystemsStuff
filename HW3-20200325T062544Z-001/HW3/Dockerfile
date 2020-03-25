FROM centos
RUN (yum install -y git java java-devel net-tools)
WORKDIR /home
RUN (cd /home; git clone https://github.com/BoiseState/CS455-resources.git)
RUN (pwd; ls)
RUN (cd /home/CS455-resources/examples/sockets/tcp/multithreaded; javac *.java)
RUN (cd /home/CS455-resources/examples/sockets/tcp/multithreaded; ls)
CMD (cd /home/CS455-resources/examples/sockets; java tcp.multithreaded.TimeServer 5005)
