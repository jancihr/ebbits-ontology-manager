package eu.om.repository;

import java.io.File;

import org.openrdf.repository.Repository;
import org.openrdf.repository.sail.SailRepository;

import com.ontotext.trree.OwlimSchemaRepository;

public class RepositoryFactory {
	public static OMRepository owlim(String location) {
		OwlimSchemaRepository sail = new OwlimSchemaRepository();
		Repository repository = new SailRepository(sail);

		sail.setParameter("build-ptsoc", "true");
		sail.setParameter("enable-optimization", "true");
		sail.setParameter("fts-memory", "0M");
		sail.setParameter("storage-folder", "bigowlim-store");
		sail.setParameter("repository-type", "file-repository");
		sail.setParameter("console-thread", "false");
		sail.setParameter("ruleset", "owl-horst");
		System.out.println("> params set");

		repository.setDataDir(new File(location));

		try{
			repository.initialize();
			return new OMRepository(repository);           
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

}
