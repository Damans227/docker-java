package DockerJava.SampleClient;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.command.PullImageResultCallback;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws InterruptedException
    {
    	Scanner in = new Scanner(System.in);
    	System.out.println("Enter the name of an image to be pulled: ");
    	
    	String pullImage = in.nextLine();
    	
    	
	
    	//*******Default Docker Client Config********
    	
    	DefaultDockerClientConfig clientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder()
    			.withDockerHost("tcp://127.0.0.1:2375")
    			//.withRegistryUsername("")
    			//.withRegistryPassword("")
    			//.withRegistryUrl("https://registry.hub.docker.com/")
    			.withDockerTlsVerify(false)
    			.build();
    	
    	DockerClient dockerClient = DockerClientBuilder.getInstance(clientConfig).build();
    	
    	/******** Pull an Image *******/
    	
    	try{
    		
    		dockerClient.pullImageCmd(pullImage).withTag("latest").exec(new PullImageResultCallback()).awaitCompletion();
    	
    	
    	
    	//******Create Containers********
    	
        CreateContainerResponse newContainer1 =
                dockerClient
                    .createContainerCmd(pullImage)
                    //.withCmd("cat", "-e")
                    .withTty(false)
                    .withStdinOpen(true)
                    .exec();
    	
    	
       /* CreateContainerResponse newContainer2 =
                dockerClient
                    .createContainerCmd("nginx")
                    .withName("Nginx")
                    .withTty(true)
                    .withStdinOpen(true)
                    .exec();*/
    	
    	//*****Start, Stop, and Kill a Container*****
    	
    	dockerClient.startContainerCmd(newContainer1.getId()).exec();
    	/*dockerClient.startContainerCmd(newContainer2.getId()).exec();*/
    	
    	//dockerClient.stopContainerCmd("0a152685482d").exec();
    	
    	//********Inspect a Container*********
    	
    	System.out.println("\n***********   CONTAINER INSPECTION   *********** \n");
    	
    	InspectContainerResponse inspectContainer1 
    	  = dockerClient.inspectContainerCmd(newContainer1.getId()).exec();
    	
    	System.out.println("Inspection Details: "+inspectContainer1.getId()+"  "+inspectContainer1.getState().getStatus());
    	
    /*	InspectContainerResponse inspectContainer2
    	= dockerClient.inspectContainerCmd(newContainer2.getId()).exec();
  	
    	System.out.println("Inspection Details: "+inspectContainer2.getId()+"  "+inspectContainer2.getState().getStatus());*/
    	
    	
    	//*********List Images**********
    	
    	
    	System.out.println("\n***********    ALL IMAGES    *********** \n");
    	
    	List<Image> images = dockerClient.listImagesCmd().withShowAll(true).exec();
    	
    	Iterator<Image> itImage = images.iterator();
    	
    	
    	while(itImage.hasNext()) {
    		
    		
    		Image image = itImage.next();
    		System.out.println(image.getId()+"  "+image.getCreated()+"  "+image.getRepoTags()[0]);
    		
    	}
    	
    	System.out.println("\n***********    ALL CONTAINERS    *********** \n");
    	
    	//*******List containers*********
    	
    	List<Container> containers = dockerClient.listContainersCmd().withShowAll(true).exec();
    	
    	//*******Iterate Containers********
    	Iterator<Container> it = containers.iterator();
    	
    	
    	while(it.hasNext()) {
    		
    		
    		Container container = it.next();
    		System.out.println(container.getImage()+"  "+container.getStatus());
    		
    	}
    	
    	
    	while(true) {
    		
    	System.out.println("\nEnter 'Y' to stop the container:");
    	String ans = in.nextLine();
    	
    	if(ans.equals("Y")) {
    		
    		dockerClient.stopContainerCmd(newContainer1.getId()).exec();
    		break;
    	}
    	
    	}
    	System.out.println("Container for "+pullImage+ " stopped!");
    	
    	
    	/******Container Stop*********/
    //	dockerClient.stopContainerCmd(newContainer1.getId()).exec();
    //	dockerClient.stopContainerCmd(newContainer2.getId()).exec();
    	
    	}
    		catch(com.github.dockerjava.api.exception.NotFoundException e) {
    		
    					System.out.println("Image for "+pullImage+" not found! Try a different image..");
    		
    			}
    	
    }
}
