package com.example.retrofit_with_recyclerview.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retrofit_with_recyclerview.R;
import com.example.retrofit_with_recyclerview.models.Employee;
import com.example.retrofit_with_recyclerview.util.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CrewListItemViewPagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Employee> employeeList;

    public CrewListItemViewPagerAdapter(List<Employee> employeeList){
        this.employeeList = employeeList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.crew_employee_card, parent, false);
        return new EmployeeCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Employee employee = employeeList.get(position);
        ImageView employeePhotoView = ((EmployeeCardViewHolder) holder).employeePhoto;
        TextView employeeNameView = ((EmployeeCardViewHolder) holder).employeeName;
        String url = Constants.API_BASE_URL + "person/" + employee.getId() + "/images";

        Picasso.get().load(url).into(employeePhotoView);
        employeeNameView.setText(employee.getName());
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    public class EmployeeCardViewHolder extends RecyclerView.ViewHolder{
        ImageView employeePhoto;
        TextView employeeName;

        public EmployeeCardViewHolder(@NonNull View itemView) {
            super(itemView);

            CardView employeeCard = itemView.findViewById(R.id.crew_employee_card);
            employeePhoto = employeeCard.findViewById(R.id.employee_photo);
            employeeName = employeeCard.findViewById(R.id.employee_name);
        }
    }
}
