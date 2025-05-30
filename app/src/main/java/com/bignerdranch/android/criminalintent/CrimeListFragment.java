package com.bignerdranch.android.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class CrimeListFragment extends Fragment {
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private boolean mSubtitleVisible;
    private Button mAddCrime;
    private TextView mNoCrimesView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        setupMenu();

        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mNoCrimesView = (TextView) view.findViewById(R.id.textView);
        mAddCrime = (Button)view.findViewById(R.id.add_a_crime);
        mAddCrime.setText(R.string.add_crime);
        mAddCrime.setEnabled(true);
        mAddCrime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                CrimeLab.get(getActivity()).setListChanged(true);
                Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
                startActivity(intent);
            }
        });

        updateUI();

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();

        updateUI();
    }

    public void setupMenu()
    {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.fragment_crime_list, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.new_crime) {
                    Crime crime = new Crime();
                    CrimeLab.get(getActivity()).addCrime(crime);
                    CrimeLab.get(getActivity()).setListChanged(true);
                    Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
                    startActivity(intent);
                    return true;
                }
                else if (item.getItemId() == R.id.show_subtitle) {
                    mSubtitleVisible = !mSubtitleVisible;
                    getActivity().invalidateOptionsMenu();
                    updateSubtitle();
                    return true;
                }
                return false;
            }
        });
    }

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent intent = CrimePagerActivity
                        .newIntent(getActivity(), crime.getId());
                startActivity(intent);
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    private void updateSubtitle() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeCount = crimeLab.getCrimes().size();
        String subtitle = getResources().getQuantityString(R.plurals.subtitle_plural, crimeCount, crimeCount);

        if (!mSubtitleVisible) {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        //Log.d("CrimeList", "The first item is " + crimes.get(0).getTitle());
        if(CrimeLab.get(getActivity()).getCrimes().size() == 0)
        {
            mNoCrimesView.setVisibility(View.VISIBLE);
            mAddCrime.setVisibility(View.VISIBLE);
        }
        else{
            mNoCrimesView.setVisibility(View.INVISIBLE);
            mAddCrime.setVisibility(View.INVISIBLE);
        }
        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        }
        else if(CrimeLab.get(getActivity()).isListChanged())
        {
            mAdapter.setCrimes(crimes);
            mAdapter.notifyDataSetChanged();
            CrimeLab.get(getActivity()).setListChanged(false);
        }
        else {
            for (int i = 0; i < crimes.size(); i++) {
                Crime crime = crimes.get(i);
                if (crime.isChanged()) {
                    mAdapter.notifyItemChanged(i);
                    crime.setChanged(false);
                    break;
                }
            }
        }
        updateSubtitle();
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private TextView mTimeTextView;
        private ImageView mSolvedImageView;
        private Crime mCrime;
        private Button mPoliceButton;


        public CrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_crime, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.crime_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.crime_date);
            mTimeTextView = (TextView) itemView.findViewById(R.id.crime_time);
            mSolvedImageView = (ImageView) itemView.findViewById(R.id.ic_solver);
        }

        public void bind(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDateString());
            mTimeTextView.setText(mCrime.getTimeString());
            mSolvedImageView.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);
            /*if (mCrime.getRequiresPolice()){
                mPoliceButton = (Button) itemView.findViewById(R.id.call_police);
                mPoliceButton.setOnClickListener(new View.OnClickListener()){
                    @Override
                    public void onClick(View v ){
                        Toast.makeText(getActivity(), "Called the police!", Toast.)
                    }
                }
            }*/
        }
            @Override
            public void onClick(View view) {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
                startActivity(intent);}
            }
    }
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {

            mCrimes = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new CrimeHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bind(crime);
        }

        @Override
        public int getItemCount() {

            return mCrimes.size();
        }

        public void setCrimes(List<Crime> crimes) {
            mCrimes = crimes;
        }

        /*@Override
        public int getItemViewType(int position){
            Crime crime = mCrimes.get(position);
            if (crime.getRequiresPolice()) {
                return 1;
            } else{
                    return 0;
            }

        }*/
    }
}

