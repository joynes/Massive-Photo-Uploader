/*********************************************************************
 * Massive Photo Uploader: Upload a batch of albums to facebook
 * Copyright (C) 2010  Johannes Kählare
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *********************************************************************/

package swe.joynes.preparator;

import java.util.List;

public class PreparedAlbum {

    private String albumName;
    private String description;
    private Boolean alreadyExists;
    private List<PreparedPhoto> photos;
  
    public PreparedAlbum(String replace, String description2, List<PreparedPhoto> files) {
        albumName = replace;
        photos = files;
        description = description2;
    }

    /**
     * Print info about this album
     */
    public String print() {
        StringBuffer str = new StringBuffer();
        str.append("\n------------------------------------");
        for (PreparedPhoto photo : photos) {
            str.append(photo.getDescription() + ": " + photo.getPath() + "\n");
        }
        str.append("------------------------------------\n");
        return str.toString();

    }

    public String getName() {
        return albumName;
    }

    public List<PreparedPhoto> getPhotos() {
        return photos;
    }

    public String getDescription() {
        return description;
    }

    public Integer getSize() {
        return photos.size();
    }

    public void setAlreadyExist(Boolean alreadyExists) {
        this.alreadyExists = alreadyExists;
    }

    public boolean getAlreadyExists() {
        return alreadyExists;
    }
}
