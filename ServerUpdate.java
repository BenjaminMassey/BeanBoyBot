package bbb;

// Sources for some code:
// Heavy use of https://github.com/hierynomus/sshj/blob/master/examples/src/main/java/net/schmizz/sshj/examples/SCPUpload.java
// https://stackoverflow.com/a/7581330 for private key usage

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.userauth.keyprovider.KeyProvider;
import net.schmizz.sshj.xfer.FileSystemFile;
import org.apache.log4j.BasicConfigurator;

import java.io.File;
import java.io.IOException;

public class ServerUpdate implements Runnable{

    public void run() {
        while(TwitchChat.connected) {
            try {
                PlayersHandler.saveAll();
                updateFile("Players.txt");
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // NOTE: All of the stuff is fairly hardcoded for right now, since I'm lazy
    // "www.speedrunstocks.com" directs to the IP of my AWS EC2 instance
    // "ubuntu" is simply the default username for such an instance
    // "BeanBotAWS.ppk" is the private key I use to login
    // "/home/ubuntu/BeanQuoteBot/" is the directory of the website
    public static void updateFile(String filename) throws IOException{
        BasicConfigurator.configure();
        SSHClient ssh = new SSHClient();
        ssh.addHostKeyVerifier(new PromiscuousVerifier());
        ssh.loadKnownHosts();
        ssh.connect("www.speedrunstocks.com");

        try {
            String username = "ubuntu";

            File privateKey = new File("BeanBotAWS.ppk");
            KeyProvider keys = ssh.loadKeys(privateKey.getPath());
            ssh.authPublickey(username, keys);

            ssh.useCompression();
            ssh.newSCPFileTransfer().upload(new FileSystemFile(filename), "/home/ubuntu/BeanQuoteBot/");
        } finally {
            ssh.disconnect();
        }
    }
}
