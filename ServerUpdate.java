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

    private static SSHClient ssh;

    public void run() {
        connectSSH();
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
    // "local.speedrunstocks.com" directs to the IP of my server (locally)
    // "ben" is the username on my server
    // "key.ppk" is the private key I use for ssh auth
    // "/home/ben/Desktop/SpeedrunStocksServer" is the directory of the website

    private static void connectSSH() {
        try {
            BasicConfigurator.configure();
            ssh = new SSHClient();
            ssh.addHostKeyVerifier(new PromiscuousVerifier());
            ssh.connect("local.speedrunstocks.com", 22);
            //ssh.connect("192.168.x.x", 22);
            //ssh.authPassword("ben", "xxxx");

            File privateKey = new File("key.ppk");
            KeyProvider keys = ssh.loadKeys(privateKey.getPath());
            ssh.authPublickey("ben", keys);

            ssh.useCompression();
        }
        catch (Exception e) {
            System.err.println("ServerUpdate.connectSSH() error : " + e.toString());
        }
    }

    private static void updateFile(String filename) throws IOException{
        try {
            ssh.newSCPFileTransfer().upload(new FileSystemFile(filename), "/home/ben/Desktop/SpeedrunStocksServer");
        } catch (Exception e) {
            System.err.println("ServerUpdate.updateFile(1) error : " + e.toString());
        }
    }

    public static void finish() {
        try {
            ssh.disconnect();
            ssh.close();
        } catch (Exception e) {
            System.err.println("ServerUpdate.finish() error : " + e.toString());
        }
    }
}
