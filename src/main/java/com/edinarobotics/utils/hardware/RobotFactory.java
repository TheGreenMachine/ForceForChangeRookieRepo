package com.edinarobotics.utils.hardware;

import com.ctre.phoenix.motorcontrol.IMotorController;
import com.ctre.phoenix.motorcontrol.IMotorControllerEnhanced;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.util.HashMap;
import java.util.Map;

public class RobotFactory {

    private Configuration config;

    public RobotFactory(String configName) {
        Yaml yaml = new Yaml(new Constructor(Configuration.class));
        config = yaml.load(
            this.getClass()
                .getClassLoader()
                .getResourceAsStream(configName + ".config.yml")
        );
    }

    public boolean isImplemented(String subsystem) {
        return (getSubsystem(subsystem) != null) && (getSubsystem(subsystem).implemented);
    }

    /**
     * @deprecated Use {@link #getMotor(String, String)} instead as this does
     * not support Victor SPXs.
     */
    @Deprecated(forRemoval = true)
    public IMotorControllerEnhanced getTalon(String subsystem, String name) {
        if (
            isImplemented(subsystem) &&
            getSubsystem(subsystem).talons.get(name) != null &&
            getSubsystem(subsystem).talons.get(name) > -1
        ) {
            return CtreMotorFactory.createDefaultTalon(getSubsystem(subsystem).talons.get(name));
        }
        return CtreMotorFactory.createGhostTalon();
    }

    /**
     * @deprecated Use {@link #getMotor(String, String, String)} instead as this does
     * not support Victor SPXs.
     */
    @Deprecated(forRemoval = true)
    public IMotorControllerEnhanced getTalon(String subsystem, String name, String master) {
        if (
            isImplemented(subsystem) &&
            getSubsystem(subsystem).talons.get(name) != null &&
            getSubsystem(subsystem).talons.get(name) > -1 &&
            getSubsystem(subsystem).talons.get(master) != null &&
            getSubsystem(subsystem).talons.get(master) > -1
        ) {
            return CtreMotorFactory.createPermanentSlaveTalon(getSubsystem(subsystem).talons.get(name), getSubsystem(subsystem).talons.get(master));
        }
        return CtreMotorFactory.createGhostTalon();
    }

    public IMotorController getMotor(String subsystem, String name) {
        if (!isImplemented(subsystem)) return CtreMotorFactory.createGhostTalon();
        Configuration.SubsystemConfig subsystemConfig = getSubsystem(subsystem);
        if (
            subsystemConfig.talons.get(name) != null &&
            subsystemConfig.talons.get(name) > -1
        ) {
            return CtreMotorFactory.createDefaultTalon(subsystemConfig.talons.get(name));
        } else if (
            subsystemConfig.victors.get(name) != null &&
            subsystemConfig.victors.get(name) > -1
        ) {
            return CtreMotorFactory.createDefaultVictor(subsystemConfig.victors.get(name));
        }
        return CtreMotorFactory.createGhostTalon();
    }

    public IMotorController getMotor(String subsystem, String name, String master) {
        if (!isImplemented(subsystem)) return CtreMotorFactory.createGhostTalon();
        Configuration.SubsystemConfig subsystemConfig = getSubsystem(subsystem);
        if (
            subsystemConfig.talons.get(name) != null &&
            subsystemConfig.talons.get(name) > -1 &&
            subsystemConfig.talons.get(master) != null &&
            subsystemConfig.talons.get(master) > -1
        ) {
            // Talons must be following another Talon, cannot follow a Victor.
            return CtreMotorFactory.createDefaultTalon(subsystemConfig.talons.get(name));
        } else if (
            subsystemConfig.victors.get(name) != null &&
            subsystemConfig.victors.get(name) > -1
        ) {
            // Victors can follow Talons or another Victor.
            if (
                subsystemConfig.talons.get(master) != null &&
                subsystemConfig.talons.get(master) > -1
            ) {
                return CtreMotorFactory.createPermanentSlaveVictor(
                        subsystemConfig.victors.get(name), subsystemConfig.talons.get(master)
                );
            } else if (
                subsystemConfig.victors.get(master) != null &&
                subsystemConfig.victors.get(master) > -1
            ) {
                return CtreMotorFactory.createPermanentSlaveVictor(
                        subsystemConfig.victors.get(name), subsystemConfig.victors.get(master)
                );
            }
        }
        return CtreMotorFactory.createGhostTalon();
    }

    public Configuration getConfig() {
        return config;
    }

    public Configuration.SubsystemConfig getSubsystem(String subsystem) {
        return config.subsystems.get(subsystem);
    }

    public static class Configuration {
        public Map<String, SubsystemConfig> subsystems;

        public static class SubsystemConfig {
            public boolean implemented = false;
            public Map<String, Integer> talons = new HashMap<>();
            public Map<String, Integer> victors = new HashMap<>();

            @Override
            public String toString() {
                return "Subsystem {\n" +
                        "  implemented = " + implemented + ",\n" +
                        "  talons = " + talons.toString() + ",\n" +
                        "  victors = " + victors.toString() + ",\n" +
                        "}";
            }
        }

        @Override
        public String toString() {
            return "Configuration {\n  subsystems = " + subsystems.toString() + "\n}";
        }
    }
}