package com.example.trevor.pianobar;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.MalformedURLException;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

import java.util.*;


public class MainActivity extends Activity {

    public static final String BROADCAST_ACTION = "RECEIVED_STATIONS";
    public static final String BROADCAST_ACTION_2 = "RECEIVED_SONG_INFO";

    private ProgressBar bar;
    private ListView stations_list;
    private TextView song;
    private TextView artist;
    private TextView album;
    private TextView time;
    private ImageButton play;
    private ImageButton pause;
    private ImageButton next;
    private FrameLayout connected;
    private TextView tvconnected;

    private String $song;
    private String $artist;
    private String $album;
    private String $time = "-0:00/0:00";

    private int progress_total = 0;
    private int progress = 0;

    private ArrayList<String> available_stations;
    private ArrayAdapter<String> adapter;

    private SocketIO socketio;

    {
        try {
            socketio = new SocketIO("http://192.168.1.92:8080");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println(available_stations.toString());
            adapter.notifyDataSetChanged();
        }
    };

    private BroadcastReceiver songInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            song.setText($song);
            artist.setText($artist);
            album.setText($album);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bar = (ProgressBar) findViewById(R.id.progressBar);
        stations_list = (ListView) findViewById(R.id.stations);
        song = (TextView) findViewById(R.id.song);
        artist = (TextView) findViewById(R.id.artist);
        album = (TextView) findViewById(R.id.album);
        time = (TextView) findViewById(R.id.time);
        play = (ImageButton) findViewById(R.id.play);
        pause = (ImageButton) findViewById(R.id.pause);
        next = (ImageButton) findViewById(R.id.next);
        connected = (FrameLayout)findViewById(R.id.connected);
        tvconnected = (TextView) findViewById(R.id.tvconnected);
        tvconnected.setText("Not Connected");
        connected.setBackgroundColor(Color.parseColor("#000000"));

        this.registerReceiver(broadcastReceiver, new IntentFilter(this.BROADCAST_ACTION));
        this.registerReceiver(songInfoReceiver, new IntentFilter(this.BROADCAST_ACTION_2));

        available_stations = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, available_stations);
        stations_list.setAdapter(adapter);

        setOnClickListeners();

        socketio.connect(new CustomIOCallback());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socketio.disconnect();
    }

    public class CustomIOCallback implements IOCallback {

        String[] items;

        @Override
        public void onMessage(JSONObject json, IOAcknowledge ack) {
            try {
                System.out.println("Server said:" + json.toString(2));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onMessage(String data, IOAcknowledge ack) {
            System.out.println("Server said: " + data);
        }

        @Override
        public void onError(SocketIOException socketIOException) {
            System.out.println("an Error occured");
            socketIOException.printStackTrace();
            socketio.disconnect();
        }

        @Override
        public void onDisconnect() {
            System.out.println("Connection terminated.");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    connected.setBackgroundColor(Color.parseColor("#000000"));
                    tvconnected.setText("Not Connected");
                }
            });

        }

        @Override
        public void onConnect() {
            System.out.println("Connection established");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    connected.setBackgroundColor(Color.parseColor("#00E800"));
                    tvconnected.setText("Connected");
                }
            });

        }

        @Override
        public void on(String event, IOAcknowledge ack, Object... args) {
            //System.out.println("Server triggered event '" + event + "'");
            String json_string = args[0].toString();
            if (event.equals("stdout")) {
                try {
                    JSONObject json = new JSONObject(json_string);
                    JSONObject data = (JSONObject) json.get("stdout");
                    JSONArray buffer = (JSONArray) data.get("data");
                    String output = "";
                    for (int i = 0; i < buffer.length(); i++) {
                        output += (char) buffer.getInt(i);
                    }
                    output = output.trim();
                    String[] list = output.split("\n");
                    if (list.length > 10) {
                        for (int i = 0; i < list.length; i++) {
                            String[] items = list[i].split(" {2,}");
                            if (items.length == 2) {
                                available_stations.add(items[1]);
                            }
                        }
                        Intent intent = new Intent(BROADCAST_ACTION);
                        sendBroadcast(intent);
                    } else {
                        items = output.split("  ");
                        if (items[0].trim().equals("[2K|>")) {
                            System.out.println(items[1]);
                            String[] descriptions = items[1].split("(\" by \"|\" on \")");
                            $song = descriptions[0].substring(1);
                            $artist = descriptions[1];
                            $album = descriptions[2].substring(0, descriptions[2].length() - 1);
                            Intent intent = new Intent(BROADCAST_ACTION_2);
                            sendBroadcast(intent);
                        } else if (items[0].trim().equals("[2K#")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (!items[1].equals($time)) {
                                        String[] values = items[1].split("/");
                                        String[] totals = values[1].split(":");
                                        try {
                                            progress_total = Integer.parseInt(totals[0]) * 60 + Integer.parseInt(totals[1]);
                                        } catch (NumberFormatException e) {
                                            e.printStackTrace();
                                        }

                                        bar.setMax(progress_total);
                                        bar.setProgress(progress);
                                        time.setText(items[1]);
                                        $time = items[1];
                                        progress++;
                                        play.setSelected(true);
                                        pause.setSelected(false);
                                    }
                                }
                            });
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setOnClickListeners() {
        stations_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                JSONObject json = new JSONObject();
                try {
                    json.put("command", position);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println(json.toString());
                socketio.emit("command", json);
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCommand("P");
                pause.setSelected(!pause.isSelected());
                play.setSelected(!play.isSelected());
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCommand("S");
                pause.setSelected(!pause.isSelected());
                play.setSelected(!play.isSelected());
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCommand("n");
                play.setSelected(false);
                pause.setSelected(false);
                bar.setProgress(0);
                progress = 0;
                progress_total = 0;
            }
        });
    }

    private void sendCommand(String command) {
        JSONObject json = new JSONObject();
        try {
            json.put("command", command);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        socketio.emit("command", json);
    }
}
