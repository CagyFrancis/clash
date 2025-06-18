package cn.njupt.xjy.analyzers;

public enum Config {
    Off,            // Disable all analysis
    IntraOnly,      // Only enable Intra-procedural analysis (Andersen-Style)
    DCallOnly,      // Only build normal call/return edges
    ICallType,      // Use FLTA to build virtual call/return edges
    ICallPointer,   // Use SVFG to build virtual call/return edges
    ICallCombined,  // Combination of FLTA and SVFG
    CSC,            // Only enable Call Store Conflict detector and Mutex tracking
    CRC,            // Only enable Call Return Conflict detector and Mutex tracking
    CBC,            // Only enable Call Back Conflict detector and Mutex tracking
    CSCMI,          // Enable CSC and Mutex Inherit
    CRCMI,          // Enable CRC and Mutex Inherit
    CBCMI,          // Enable CBC and Mutex Inherit
    Clash,          // Enable Clash framework with SVFG
    ClashType,      // Enable Clash framework with FLTA
    ClashCombined   // Enable Clash framework with combination of FLTA and SVFG
}