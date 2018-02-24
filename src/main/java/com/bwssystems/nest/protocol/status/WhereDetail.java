
package com.bwssystems.nest.protocol.status;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class WhereDetail {

    @SerializedName("$version")
    @Expose
    private Long $version;
    @SerializedName("$timestamp")
    @Expose
    private Long $timestamp;
    @SerializedName("wheres")
    @Expose
    private List<WhereItem> wheres = new ArrayList<WhereItem>();

    
    private String strOriginalJSON = null;

//    public WhereDetail( final String strOriginalJSON ) {
//    	this.strOriginalJSON = strOriginalJSON;
//    }
    
    public void setOriginalJSON( final String strOriginalJSON ) {
    	this.strOriginalJSON = strOriginalJSON;
    }

    public String getOriginalJSON() {
    	return this.strOriginalJSON;
    }

    
    /**
     * 
     * @return
     *     The $version
     */
    public Long get$version() {
        return $version;
    }

    /**
     * 
     * @param $version
     *     The $version
     */
    public void set$version(Long $version) {
        this.$version = $version;
    }

    /**
     * 
     * @return
     *     The $timestamp
     */
    public Long get$timestamp() {
        return $timestamp;
    }

    /**
     * 
     * @param $timestamp
     *     The $timestamp
     */
    public void set$timestamp(Long $timestamp) {
        this.$timestamp = $timestamp;
    }

    /**
     * 
     * @return
     *     The wheres
     */
    public List<WhereItem> getWheres() {
        return wheres;
    }

    /**
     * 
     * @param wheres
     *     The wheres
     */
    public void setWheres(List<WhereItem> wheres) {
        this.wheres = wheres;
    }

}
