package com.example.TalkToDo.service;

import com.example.TalkToDo.entity.Meeting;
import com.example.TalkToDo.entity.TranscriptLine;
import com.example.TalkToDo.repository.TranscriptLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TranscriptLineService {

    @Autowired
    private TranscriptLineRepository transcriptLineRepository;

    public List<TranscriptLine> getAllTranscriptLines() {
        return transcriptLineRepository.findAll();
    }

    public List<TranscriptLine> getTranscriptLineById(Long meetingId) {
        Meeting meeting = Meeting.builder().id(meetingId).build();
        return transcriptLineRepository.findByMeeting(meeting);
    }

    public TranscriptLine createTranscriptLine(TranscriptLine transcriptLine) {
        return transcriptLineRepository.save(transcriptLine);
    }

    public List<TranscriptLine> updateTranscriptLine(List<TranscriptLine> transcriptLineList) {
        return transcriptLineRepository.saveAll(transcriptLineList);
    }

    @Transactional
    public boolean deleteTranscriptLine(Long id) {
        return transcriptLineRepository.findById(id)
                .map(line -> {
                    transcriptLineRepository.delete(line);
                    return true;
                })
                .orElse(false);
    }
} 