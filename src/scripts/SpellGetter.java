package scripts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;


public class SpellGetter {

	public static void main(String[] args){

		getPage("http://www.google.com");

	}

	public static void getPage(String weburl) {
		try {
			URL url = new URL(weburl);
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}

			reader.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}  catch (IOException e) {
			e.printStackTrace();
		}
	}
}
