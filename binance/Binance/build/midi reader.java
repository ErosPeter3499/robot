import javax.sound.midi.*;
import java.util.Scanner;

public class MidiReader {

    public static void main(String[] args) {
        try {
            // List available MIDI devices
            MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
            for (int i = 0; i < infos.length; i++) {
                System.out.println(i + ": " + infos[i].getName());
            }

            // Prompt user to select a MIDI device
            System.out.print("Enter the number of the MIDI input device to use: ");
            Scanner scanner = new Scanner(System.in);
            int deviceNumber = scanner.nextInt();

            // Open the selected MIDI device
            MidiDevice device = MidiSystem.getMidiDevice(infos[deviceNumber]);
            device.open();

            // Get the device's transmitter and set a receiver to handle incoming messages
            Transmitter transmitter = device.getTransmitter();
            transmitter.setReceiver(new MidiInputReceiver(device.getDeviceInfo().toString()));

            System.out.println("Listening for MIDI input on " + device.getDeviceInfo().getName() + "...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class MidiInputReceiver implements Receiver {
        private String name;

        public MidiInputReceiver(String name) {
            this.name = name;
        }

        @Override
        public void send(MidiMessage message, long timeStamp) {
            if (message instanceof ShortMessage) {
                ShortMessage sm = (ShortMessage) message;
                switch (sm.getCommand()) {
                    case ShortMessage.NOTE_ON:
                        System.out.println("Note ON: " + sm.getData1() + " Velocity: " + sm.getData2());
                        break;
                    case ShortMessage.NOTE_OFF:
                        System.out.println("Note OFF: " + sm.getData1() + " Velocity: " + sm.getData2());
                        break;
                }
            }
        }

        @Override
        public void close() {
        }
    }
}