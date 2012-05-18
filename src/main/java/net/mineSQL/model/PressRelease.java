/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.mineSQL.model;
import java.util.Date;

import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

@Node
public class PressRelease
{
	@Field(path=true) String path;
	@Field String title;
	@Field Date pubDate;
	@Field String content;

}
