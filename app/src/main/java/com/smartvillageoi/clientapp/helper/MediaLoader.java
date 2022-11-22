/*
 * Copyright 2017 Yan Zhenjie.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.smartvillageoi.clientapp.helper;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.smartvillageoi.clientapp.R;
import com.smartvillageoi.clientapp.helper.album.AlbumFile;
import com.smartvillageoi.clientapp.helper.album.AlbumLoader;


public class MediaLoader implements AlbumLoader {

    @Override
    public void load(ImageView imageView, AlbumFile albumFile) {
        load(imageView, albumFile.getPath());
    }

    @Override
    public void load(ImageView imageView, String url) {
        //Picasso.get().load(url).placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).into(imageView);
        Glide.with(imageView.getContext())
                .load(url)
                .apply(new RequestOptions()
                        .error(R.mipmap.ic_launcher)
                        .placeholder(R.mipmap.ic_launcher)
                        .centerCrop()
                )
                .into(imageView);
    }
}