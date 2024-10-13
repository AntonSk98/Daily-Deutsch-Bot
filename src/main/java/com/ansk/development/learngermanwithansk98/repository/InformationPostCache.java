package com.ansk.development.learngermanwithansk98.repository;

import com.ansk.development.learngermanwithansk98.service.model.output.InformationPostModel;
import org.springframework.stereotype.Component;

/**
 * Cache to store {@link InformationPostModel}.
 *
 * @author Anton Skripin
 */
@Component
public class InformationPostCache {
    private InformationPostModel informationPost;

    public void saveInformationPost(InformationPostModel informationPost) {
        this.informationPost = informationPost;
    }

    public InformationPostModel getInformationPost() {
        return informationPost;
    }

    public void clear() {
        informationPost = null;
    }
}
