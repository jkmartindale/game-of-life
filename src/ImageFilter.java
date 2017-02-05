import java.io.File;
import javax.swing.filechooser.FileFilter;
//View filter for file picker
public class ImageFilter extends FileFilter
{
	public boolean accept(File file)
	{
		if (file.isDirectory())
			return true;

		int i = file.getName().lastIndexOf('.');
		if (i >= 0)
		{
			String extension = file.getName().substring(i + 1).toLowerCase();
			if (extension.equals("png") ||
				extension.equals("jpg") ||
				extension.equals("jpeg") ||
				extension.equals("gif") ||
				extension.equals("bmp") ||
				extension.equals("tif") ||
				extension.equals("tiff"))
			return true;
		}

		return false;
	}

	public String getDescription() { return "Images (*.png;*.jpg;*.jpeg;*.bmp;*.tif;*.tiff)"; }

}