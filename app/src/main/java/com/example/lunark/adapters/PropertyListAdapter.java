package com.example.lunark.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lunark.R;
import com.example.lunark.models.Property;
import com.example.lunark.util.ClientUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PropertyListAdapter extends BaseAdapter {
    private Activity activity;
    private final List<Property> properties;

    public PropertyListAdapter(Activity activity, List<Property> properties) {
        this.activity = activity;
        this.properties = properties;
    }

    @Override
    public int getCount() {
        return properties.size();
    }

    @Override
    public Object getItem(int position) {
        return properties.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        Property property = properties.get(position);

        if (convertView == null)
            vi = activity.getLayoutInflater().inflate(R.layout.property_card, null);

        TextView name = (TextView) vi.findViewById(R.id.name);
        TextView location = (TextView) vi.findViewById(R.id.location);
        TextView description = (TextView) vi.findViewById(R.id.description);
        TextView price = (TextView) vi.findViewById(R.id.price);
        ImageView thumbnail = (ImageView) vi.findViewById(R.id.thumbnail);

        name.setText(property.getName());
        location.setText(property.getAddress().toString());
        description.setText(property.getDescription());

        if (property.getImages().size() > 0) {
            Call<ResponseBody> call = ClientUtils.propertyService.getImage(property.getId(), property.getImages().get(0).getId());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        if (response.body() != null) {
                            Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                            thumbnail.setImageBitmap(bmp);
                        } else {
                            Log.d("REZ", "Response body is null.");
                        }
                    } else {
                        Log.d("REZ", "Message received: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
                }
            });
        }

        return vi;
    }
}

