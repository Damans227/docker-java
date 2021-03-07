package sample2.sample2;

import java.util.Iterator;
import java.util.List;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;

/**
 * Hello world!
 *
 */
public class App 
{

    public static void main( String[] args )
    {
    	
    	
    	//Default Docker Client Config
    	
    	DefaultDockerClientConfig clientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder()
    			.withDockerHost("tcp://127.0.0.1:2375").build();
    	DockerClient client = DockerClientBuilder.getInstance(clientConfig).build();
    	
    	//List containers
    	
    	List<Container> containers = client.listContainersCmd().withShowAll(true).exec();
    	
    	//Iterate Containers
    	Iterator<Container> it = containers.iterator();
    	
    	
    	while(it.hasNext()) {
    		
    		
    		Container container = it.next();
    		System.out.println(container.getImage()+"  "+container.getStatus());
    		
    	}
    	
    	
    	//client.startContainerCmd(containers.get(0).getId()).exec();
    	
    	//client.stopContainerCmd(containers.get(0).getId()).exec();

    }
    

}
