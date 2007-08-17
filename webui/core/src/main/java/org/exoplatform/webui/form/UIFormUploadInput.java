/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.webui.form;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.exoplatform.upload.UploadResource;
import org.exoplatform.upload.UploadService;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;

/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Jun 6, 2006
 * 
 * Represents an upload form
 */

@ComponentConfig(template = "system:/groovy/webui/form/UIFormUploadInput.gtmpl")    
public class UIFormUploadInput extends UIFormInputBase<String> {
  /**
   * The current upload id
   */
  private String uploadId_ ;
  /**
   * The resource to upload
   */
  private UploadResource uploadResource_ ;
  
  public UIFormUploadInput(String name, String bindingExpression) {
    super(name, bindingExpression, String.class);
    uploadId_ = Integer.toString(Math.abs(hashCode())) ;
    setComponentConfig(UIFormUploadInput.class, null) ;
  }
  
  @SuppressWarnings("unused")
  public void decode(Object input, WebuiRequestContext context) throws Exception {
    uploadResource_ = null ;
    boolean hasUpload = "true".equals(input) ;
    if(hasUpload) {
      UploadService service = getApplicationComponent(UploadService.class) ;
      uploadResource_ = service.getUploadResource(uploadId_) ;
      System.out.println("upload stores at " + uploadResource_.getStoreLocation()) ;
    }
  }
  
  public InputStream getUploadDataAsStream() throws Exception {
    if(uploadResource_ == null) return null;
    File file = new File(uploadResource_.getStoreLocation());
    return new FileInputStream(file);
  }
  
  public byte[] getUploadData() throws Exception {
    if(uploadResource_ == null) return null;
    File file = new File(uploadResource_.getStoreLocation());
    FileInputStream inputStream =  new FileInputStream(file);
    FileChannel fchan = inputStream.getChannel();
    long fsize = fchan.size();       
    ByteBuffer buff = ByteBuffer.allocate((int)fsize);        
    fchan.read(buff);
    buff.rewind();      
    byte[] data = buff.array();      
    buff.clear();      
    fchan.close();        
    inputStream.close();       
    return data;
  }
  
  public String getUploadId() { return uploadId_; }
  
  public String getActionUpload(){
    WebuiRequestContext context = WebuiRequestContext.getCurrentInstance();
    WebuiRequestContext pcontext = (WebuiRequestContext)context.getParentAppRequestContext();
    if(pcontext == null) pcontext = context;
//    String uploadAction = pcontext.getRequestContextPath() + "/upload?uploadId=" + uploadId_+"&action=upload" ;
    String uploadAction = pcontext.getRequestContextPath() + "/command?";
    uploadAction += "type=org.exoplatform.web.command.handler.UploadHandler";
    uploadAction += "&uploadId=" + uploadId_+"&action=upload" ;
    return uploadAction;
  }

  public UploadResource getUploadResource() { return uploadResource_; }

}