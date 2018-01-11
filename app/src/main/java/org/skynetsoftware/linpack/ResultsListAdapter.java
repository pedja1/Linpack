/*
* This file is part of the Kernel Tuner.
*
* Copyright Predrag Čokulov <predragcokulov@gmail.com>
*
* Kernel Tuner is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* Kernel Tuner is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with Kernel Tuner. If not, see <http://www.gnu.org/licenses/>.
*/
package org.skynetsoftware.linpack;


import android.content.*;
import android.view.*;
import android.widget.*;

import java.util.Locale;

final class ResultsListAdapter extends ArrayAdapter<Result>
{
    ResultsListAdapter(final Context context)
    {
        super(context, 0);
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent)
    {

        final View view = getWorkingView(parent, convertView);
        final ViewHolder viewHolder = getViewHolder(view);
        final Result entry = getItem(position);

        viewHolder.mflopsView.setText(String.format(Locale.US, "%.3f", entry.mflops));
        viewHolder.dateView.setText(String.valueOf(entry.date));


        return view;
    }

    private View getWorkingView(ViewGroup parent, final View convertView)
    {
        View workingView;

        if (null == convertView)
        {
            final Context context = getContext();
            final LayoutInflater inflater = (LayoutInflater) context.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);

            workingView = inflater.inflate(R.layout.results_row, parent, false);
        }
        else
        {
            workingView = convertView;
        }

        return workingView;
    }

    private ViewHolder getViewHolder(final View workingView)
    {
        final Object tag = workingView.getTag();
        ViewHolder viewHolder;


        if (null == tag || !(tag instanceof ViewHolder))
        {
            viewHolder = new ViewHolder();

            viewHolder.mflopsView = (TextView) workingView.findViewById(R.id.mflops);
            viewHolder.dateView = (TextView) workingView.findViewById(R.id.date);

            workingView.setTag(viewHolder);

        }
        else
        {
            viewHolder = (ViewHolder) tag;
        }

        return viewHolder;
    }

    private class ViewHolder
    {
        TextView mflopsView;
        TextView dateView;
    }


}
